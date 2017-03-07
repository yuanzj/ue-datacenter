package net.rokyinfo.receive.handler;


import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.entity.UEServerMsg;
import net.rokyinfo.basedao.dao.UEServerMsgDao;
import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.receive.bean.BasePacket;
import net.rokyinfo.receive.bean.ServerPacket;
import net.rokyinfo.receive.writepacket.WritePacketStrategy;
import org.slf4j.LoggerFactory;

public class ServerPacketCommand implements PacketCommand {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(UEPacketCommand.class);

    /**
     * 报文写策略
     */
    private WritePacketStrategy writePacketStrategy;

    /**
     * 写入数据库实体的id
     */
    private Long id;

    public void parsePacket(ChannelHandlerContext ctx, BasePacket basePacket) {

        ServerPacket serverPacket = (ServerPacket) basePacket;

        logger.info("发送消息给UE完成:" + serverPacket.toString());

        Pojo pojo = null;

        try {
            pojo = fromPacketToEntity(serverPacket);
        } catch (Exception e) {

            e.printStackTrace();
            logger.info("报文：" + serverPacket.toString() + "无法转换为合法的实体对象");

            return;
        }

//		writePacketStrategy.writePacket(basePacket);
        UEServerMsgDao ueServerMsgDao = (UEServerMsgDao) SpringContainer.getSpringContainer().getBean("ueServerMsgInfoDao");
        UEServerMsg ueServerMsgInfo = (UEServerMsg) pojo;
        ueServerMsgDao.addUEServerMsg(ueServerMsgInfo);

        this.id = ueServerMsgInfo.getId();

        // 发送消息给UE
        ctx.writeAndFlush(serverPacket.toString());
    }

    protected Pojo fromPacketToEntity(ServerPacket serverPacket) throws Exception {

        UEServerMsg ueServerMsg = new UEServerMsg();
        ueServerMsg.setUeSn(serverPacket.getUeSn());
        ueServerMsg.setMessageType(serverPacket.getPacketType());
        ueServerMsg.setCreateTime(serverPacket.getTime());
        ueServerMsg.setAgreementVersion("R1");

        serverPacket.setUePacketEntity(ueServerMsg);

        return ueServerMsg;
    }

    public WritePacketStrategy getWritePacketStrategy() {
        return writePacketStrategy;
    }

    public void setWritePacketStrategy(WritePacketStrategy writePacketStrategy) {
        this.writePacketStrategy = writePacketStrategy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
