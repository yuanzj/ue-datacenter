package net.rokyinfo.receive.handler;


import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.dao.UEBasicInfoDao;
import net.rokyinfo.basedao.dao.UERK600Dao;
import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UEBasicInfo;
import net.rokyinfo.basedao.entity.UERK600;
import net.rokyinfo.receive.bean.BasePacket;
import net.rokyinfo.receive.bean.UEPacket;
import net.rokyinfo.receive.handler.packet.ChannelHandlerContextContainer;
import net.rokyinfo.receive.handler.packet.T1PacketCommand;
import net.rokyinfo.receive.redis.UpdateRedisFromLocalMemory;
import net.rokyinfo.receive.redis.UpdateUEStatusStrategy;
import net.rokyinfo.receive.writepacket.WritePacketStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;


/**
 * ue端上发报文处理基类
 *
 * @author Administrator
 */
public abstract class UEPacketCommand implements PacketCommand {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(UEPacketCommand.class);

    /**
     * 报文写策略
     */
    private WritePacketStrategy writePacketStrategy;

    /**
     * 更新设备状态策略
     */
    private UpdateUEStatusStrategy updateUEStatusStrategy;

    protected final String REPLY_SEPERATOR = "==========>";

    private String replyToClientMsg;

    private UERK600 ueRK600;

    private UEBasicInfo ueBasicInfo;

    private static UEBasicInfoDao ueBasicInfoDao = (UEBasicInfoDao) SpringContainer
            .getSpringContainer().getBean("ueBasicInfoDao");

    private static UERK600Dao ueRK600Dao = (UERK600Dao) SpringContainer
            .getSpringContainer().getBean("ueRK600Dao");

    public void parsePacket(ChannelHandlerContext ctx, BasePacket basePacket) {

        UEPacket uePacket = (UEPacket) basePacket;

        ueRK600 = ueRK600Dao.getUERK600ByIMEI(uePacket.getImei());

        // 对报文进行预处理
        if (!prePacket(ctx, uePacket)) {

            /** 返回失败给客户端 */
            replyToClientFailure(ctx, uePacket);

            // 关闭连接
            ctx.close();
            return;
        }

        if (ueRK600 == null) {

            /** 返回失败给客户端 */
            replyToClientFailure(ctx, uePacket);

            // 关闭连接
            ctx.close();
            return;
        }

        uePacket.setUeSn(ueRK600.getUeSn());

        ueBasicInfo = ueRK600.getUeBasicInfo();

        if (StringUtils.isEmpty(StringUtils.trim(uePacket.getCcSn()))) {

            if (ueBasicInfo != null) {
                uePacket.setCcSn(ueBasicInfo.getUeSn());
            }
        } else {

            if (ueBasicInfo == null || !uePacket.getCcSn().equals(ueBasicInfo.getUeSn())) {

                ueBasicInfo = ueBasicInfoDao.getUEBasicInfoBySn(uePacket.getCcSn());
                if (ueBasicInfo == null) {

                    /** 返回失败给客户端 */
                    replyToClientFailure(ctx, uePacket);

                    // 关闭连接
                    ctx.close();
                    return;
                }

                ueRK600Dao.clearCC(ueBasicInfo.getId());
                ueRK600Dao.relateCC(ueBasicInfo.getId(), ueRK600.getId());
            }
        }

        // 判断是否登录
        if (!isLogin(ctx, uePacket)) {

            /** 返回失败给客户端 */
            replyToClientFailure(ctx, uePacket);

            // 关闭连接
            ctx.close();
            return;
        }

        try {
            fromPacketToEntity(uePacket);
        } catch (Exception e) {

            logger.error("报文：" + uePacket.toString() + "无法转换为合法的实体对象");
            logger.error(e.getMessage());
            e.printStackTrace();

            /** 返回失败给客户端 */
            replyToClientFailure(ctx, uePacket);

            if (this instanceof T1PacketCommand) {
                // 关闭连接
                ctx.close();
            }

            return;
        }

        /** 返回成功给客户端 */
        replyToClientSuccess(ctx, uePacket);

        // 写报文。其中应答类的报文不需要定义写策略，只需要更新t_ue_server_msg_report表即可
        writePacket(uePacket);

        // 报文后续处理
        postPacket(ctx, uePacket);

        // 更新缓存状态
        updateStatusToCache(uePacket);
    }

    /**
     * 存储报文
     *
     * @param uePacket
     */
    protected void writePacket(UEPacket uePacket) {
        if (this.writePacketStrategy != null) {
            writePacketStrategy.writePacket(uePacket);
        }
    }

    /**
     * 对报文进行预处理
     *
     * @param uePacket
     */
    public boolean prePacket(ChannelHandlerContext ctx, UEPacket uePacket) {

        logger.info("对报文:" + uePacket.toString() + "，from:" + ctx.channel().remoteAddress().toString() + "进行预处理");

        /**
         * 是否登录的判断
         */
        return true;
    }

    /**
     * 进行是否登录的判断 是否登录的判断采用MAP中是否有此sn为key的数据
     *
     * @return
     */
    protected boolean isLogin(ChannelHandlerContext ctx, UEPacket uePacket) {

        logger.info("对报文：" + uePacket.toString() + "，from" + ctx.channel().remoteAddress().toString() + "进行登陆判断");

        String ueSn = uePacket.getUeSn();
        ChannelHandlerContextContainer handlerContextContainer = ChannelHandlerContextContainer.getHandlerContainer();
        ChannelHandlerContext oldCtx = handlerContextContainer.getHandlerContext(ueSn);

        if (oldCtx == null) {

            logger.info("报文：" + uePacket.toString() + "，from" + ctx.channel().remoteAddress().toString() + "没有登陆");
            return false;
        }

        if (ctx != oldCtx) {

            handlerContextContainer.putHandlerContext(ueSn, ctx);
            oldCtx.channel().close();
        }

        return true;
    }

    public void sendReplyMsgToWeb(String ueSn, String val[], String packet) {

    }

    /**
     * 告警短消息发送策略
     *
     * @param messageType
     * @param messageKey
     * @return boolean 返回true表示此次需要发送消息
     */
    protected boolean messageStrategy(UEPacket uePacket, String messageType, String messageKey, Long ownerMobile) {

        return true;
    }

    /**
     * 发送告警短消息给用户
     *
     * @param messageType
     * @param messageKey
     */
    protected void sendMessage(UEPacket uePacket, String messageType,
                               String messageKey) {

    }

    /**
     * 报文处理完之后的后续处理，如返回消息给客户端等
     */
    protected void postPacket(ChannelHandlerContext ctx, UEPacket uePacket) {

        logger.info("对报文：" + uePacket.toString() + "，from" + ctx.channel().remoteAddress().toString() + "进行后处理");
    }

    protected void replyToClientSuccess(ChannelHandlerContext ctx,
                                        UEPacket uePacket) {

        logger.info("报文处理成功，返回客户端");

        getDetailReplyToClientMsg(true);
        logger.info(uePacket.toString() + "，from" + ctx.channel().remoteAddress().toString() + this.REPLY_SEPERATOR
                + replyToClientMsg);

        if (this.replyToClientMsg != null) {

            ctx.writeAndFlush(this.replyToClientMsg);
        }
    }

    protected void replyToClientFailure(ChannelHandlerContext ctx,
                                        UEPacket uePacket) {

        logger.info("报文" + uePacket.toString() + "处理失败，不返回客户端");
    }

    protected void getDetailReplyToClientMsg(boolean isReplySuccess) {

    }

    /**
     * 根据报文更新缓存中的ue status
     *
     * @param uePacket
     */
    protected void updateStatusToCache(UEPacket uePacket) {

        this.updateUEStatusStrategy.updateRedis(uePacket);
    }

    protected Pojo fromPacketToEntity(UEPacket uePacket)
            throws Exception {

        return null;
    }

    public WritePacketStrategy getWritePacketStrategy() {
        return writePacketStrategy;
    }

    public void setWritePacketStrategy(WritePacketStrategy writePacketStrategy) {
        this.writePacketStrategy = writePacketStrategy;
    }

    public UpdateUEStatusStrategy getUpdateUEStatusStrategy() {
        return updateUEStatusStrategy;
    }

    public void setUpdateUEStatusStrategy(
            UpdateRedisFromLocalMemory updateUEStatusStrategy) {

        this.updateUEStatusStrategy = updateUEStatusStrategy;
    }

    public String getReplyToClientMsg() {
        return replyToClientMsg;
    }

    public void setReplyToClientMsg(String replyToClientMsg) {
        this.replyToClientMsg = replyToClientMsg;
    }

    public UEBasicInfo getUeBasicInfo() {
        return ueBasicInfo;
    }

    public void setUeBasicInfo(UEBasicInfo ueBasicInfo) {
        this.ueBasicInfo = ueBasicInfo;
    }

    public UERK600 getUeRK600() {
        return ueRK600;
    }

    public void setUeRK600(UERK600 ueRK600) {
        this.ueRK600 = ueRK600;
    }
}
