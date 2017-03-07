package net.rokyinfo.receive.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import net.rokyinfo.basecommon.constant.RedisPrefixConstants;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basecommon.util.StringUtils;
import net.rokyinfo.receive.bean.UEPacket;
import net.rokyinfo.receive.service.UEPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ReceiveHandler extends SimpleChannelInboundHandler<String> {

    private final static Logger logger = LoggerFactory.getLogger(ReceiveHandler.class);

    private UEPacketService uePacketService = (UEPacketService) SpringContainer
            .getSpringContainer().getBean("uePacketService");

    @SuppressWarnings("deprecation")
    private static final AttributeKey<ChannelState> STATE = new AttributeKey<ChannelState>(
            "TcpHandler.state");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        logger.info("===============The Message Receive is:===============");
        logger.info(msg);

        try {

            // 按 ]解析msg,是否是多包
            String[] msgArray = msg.trim().split("]");
            if (msgArray.length > 0) {

                for (int i = 0; i < msgArray.length; i++) {

                    String packet = StringUtils.replaceTab(msgArray[i]);
                    String[] requestArray = packet.split("\\,");

                    if (requestArray.length < 6) {

                        logger.info("report packet is illegal!");

                        // 关闭连接
                        ctx.close();
                        continue;
                    }

                    if (!"R1".equals(requestArray[2])) {

                        logger.info("protocol is illegal!");

                        // 关闭连接
                        ctx.close();
                        continue;
                    }

                    // 请求报文类型
                    String messageType = requestArray[5];

                    if (messageType.startsWith("T")) {

                        UEPacket uePacket = new UEPacket(packet);
                        if (!uePacket.isMessageCorrect()) {
                            logger.info("protocol is illegal!");

                            // 关闭连接
                            ctx.close();
                            continue;
                        }

                        ChannelState channelState = new ChannelState();
                        channelState.setUeSn(uePacket.getCcSn());

                        ctx.attr(STATE).set(channelState);

                        uePacketService.handlePacket(ctx, uePacket);

                        uePacket = null;
                    } else {

                        // 关闭连接
                        ctx.close();
                        logger.info("==illegal connection===");
                        continue;
                    }
                }
            }
        } finally {

            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        ChannelState channelState = ctx.attr(STATE).get();

        if (channelState != null) {

            String ccSN = channelState.getUeSn();

            // 将消息存入redis
            ShardedJedisPool shardedJedisPool = SpringContainer.getSpringContainer().getShardedJedisPool();
            ShardedJedis jedis = shardedJedisPool.getResource();

            try {

                // 删除所有在线的集合
                jedis.srem(RedisPrefixConstants.ALL_ONLINE_UE, ccSN);

                jedis.del(RedisPrefixConstants.ONLINE_RK610_UE + ccSN);

            } finally {

                shardedJedisPool.returnResource(jedis);
            }
        }

        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        if (cause instanceof java.io.IOException) {

            logger.error(cause.getMessage());
            return;
        }

        super.exceptionCaught(ctx, cause);
    }
}
