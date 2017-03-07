package net.rokyinfo.receive.listener;


import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.receive.bean.ServerPacket;
import net.rokyinfo.receive.handler.ServerPacketCommand;
import net.rokyinfo.receive.handler.packet.ChannelHandlerContextContainer;
import net.rokyinfo.receive.util.PoolMQSender;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandToUEListener implements MessageListener {

    private PoolMQSender poolMQSender;

    private static Logger logger = Logger.getLogger(CommandToUEListener.class);

    public static Map<String, String[]> map = new ConcurrentHashMap<String, String[]>();

    public CommandToUEListener() throws JMSException {

        super();

        poolMQSender = (PoolMQSender) SpringContainer.getSpringContainer().getBean("poolMQSender");

        Connection connection = poolMQSender.getMQConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("CMD_TO_UE");

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);
    }

    public void onMessage(Message message) {

        try {
            String packet = message.getStringProperty("packet");
            String hostName = message.getStringProperty("hostName");
            long userMessageId = message.getLongProperty("userMessageId");
            String ueSn = message.getStringProperty("sn");

            ChannelHandlerContext ctx = ChannelHandlerContextContainer.getHandlerContainer().getHandlerContext(ueSn);

            // ChannelHandlerContext为空表示此sn的句柄不在这个TCPServer上，不进行处理
            if (ctx == null) {

                return;
            }

            logger.info("收到需要发送给UE的消息：" + packet);

            ServerPacket serverPacket = new ServerPacket(packet);
            serverPacket.setUeSn(ueSn);
            ServerPacketCommand serverPacketCommand = new ServerPacketCommand();
            serverPacketCommand.parsePacket(ctx, serverPacket);

            String val[] = {hostName, String.valueOf(userMessageId), String.valueOf(serverPacketCommand.getId())};
            map.put(ueSn, val);

            logger.info("command全局变量：" + val);
            for (int i = 0; i < val.length; i++) {

                logger.info(val[i]);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
