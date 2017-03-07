package net.rokyinfo.receive.handler.packet;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.basecommon.constant.RedisPrefixConstants;
import net.rokyinfo.basecommon.config.Config;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.dao.UERK600Dao;
import net.rokyinfo.basedao.dao.UESimDao;
import net.rokyinfo.basedao.entity.*;
import net.rokyinfo.model.EbikeListProtos;
import net.rokyinfo.receive.bean.UEPacket;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/24.
 */
public class T1PacketCommand extends net.rokyinfo.receive.handler.UEPacketCommand {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(T1PacketCommand.class);

    private static final int ONLINE_UE_EXPIRED = Integer.parseInt(Config.getConfig().getParameter("ONLINE_UE_EXPIRED"));

    private static final int UPLOAD_SN_EXPIRED = Integer.parseInt(Config.getConfig().getParameter("UPLOAD_SN_EXPIRED"));

    private int loginResult = 0;

    private UEFirm firm;

    /**
     * 对T1报文进行预处理
     *
     * @param uePacket
     */
    @Override
    public boolean prePacket(ChannelHandlerContext ctx, final UEPacket uePacket) {

        logger.info("对报文：" + uePacket.toString() + "，from:" + ctx.channel().remoteAddress().toString() + "进行预处理");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());

        final UERK600Dao ueRK600Dao = (UERK600Dao) SpringContainer
                .getSpringContainer().getBean("ueRK600Dao");

        final UESimDao simDao = (UESimDao) SpringContainer.getSpringContainer()
                .getBean("simDao");

        final UERK600 ueRK600 = this.getUeRK600();

        if (ueRK600 == null) {
            logger.info("报文对应的sn不存在");
            setReplyToClientMsg("[" + time + ",S1,3]");
            loginResult = 3;
            return false;
        }

        this.firm = ueRK600.getFirm();
        // T1报文中的企业信息错误，则不允许登陆
        if (firm == null) {

            logger.info("报文对应的企业不存在");
            setReplyToClientMsg("[" + time + ",S1,0]");
            loginResult = 0;
            return false;
        }

        String imsi = uePacket.getParams()[0];

        // 更新设备的SIM卡信息
        if (ueRK600.getSim() == null || !ueRK600.getSim().getImsi().equals(imsi)) {

            UESim orisim = simDao.getUESimByImsi(imsi);
            if (orisim != null) {
                ueRK600Dao.updateSimInfo(ueRK600.getId(), orisim.getId());
            }
        }

        loginResult = 1;

        return true;
    }

    @Override
    protected void getDetailReplyToClientMsg(boolean isReplySuccess) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());

        if (isReplySuccess) {

            setReplyToClientMsg("[" + time + ",S1,1]");
        } else {

            if (getReplyToClientMsg() == null) {

                setReplyToClientMsg("[" + time + ",S1,0]");
            }
        }
    }

    @Override
    protected void replyToClientFailure(ChannelHandlerContext ctx,
                                        UEPacket uePacket) {

        logger.info("登陆失败，返回客户端");

        logger.info(uePacket.toString() + this.REPLY_SEPERATOR
                + super.getReplyToClientMsg());

        getDetailReplyToClientMsg(false);
        ctx.writeAndFlush(super.getReplyToClientMsg());

        // 失败之后，将登陆失败的报文也写进login表中
        fromPacketToEntity(uePacket);

        super.getWritePacketStrategy().writePacket(uePacket);
    }

    /**
     * 进行是否登录的判断，T1报文不需要进行登陆的判断
     * 是否登录的判断采用MAP中是否有此sn为key的数据
     *
     * @return
     */
    @Override
    protected boolean isLogin(ChannelHandlerContext ctx, UEPacket uePacket) {

        logger.info("对报文：" + uePacket.toString() + "进行登陆判断");

        String ueSn = uePacket.getUeSn();
        ChannelHandlerContextContainer handlerContextContainer = ChannelHandlerContextContainer
                .getHandlerContainer();

        ChannelHandlerContext oldCtx = handlerContextContainer
                .getHandlerContext(ueSn);

        if (oldCtx != null && ctx != oldCtx) {

            handlerContextContainer.removeHandlerContext(ueSn);
            oldCtx.channel().close();
        }

        handlerContextContainer.putHandlerContext(ueSn, ctx);

        return true;
    }

    @Override
    protected Pojo fromPacketToEntity(UEPacket uePacket) {

        UELogin ueLogin = new UELogin();
        try {

            ueLogin.setUeSn(uePacket.getUeSn());
            ueLogin.setCcSn(uePacket.getCcSn());
            ueLogin.setCreateTime(uePacket.getTime());
            ueLogin.setImsi(uePacket.getParams()[0]);
            ueLogin.setCarStatus(uePacket.getParams()[1]);
            ueLogin.setLoginResult(this.loginResult);

        } catch (Exception e) {
            e.printStackTrace();
        }

        uePacket.setPojo(ueLogin);

        return ueLogin;
    }

    /**
     * 管理RK600和中控
     * @param ctx
     * @param uePacket
     */
    @Override
    protected void postPacket(ChannelHandlerContext ctx, UEPacket uePacket) {

        super.postPacket(ctx, uePacket);

    }

    @Override
    protected void updateStatusToCache(UEPacket uePacket) {

        ShardedJedisPool shardedJedisPool = SpringContainer.getSpringContainer().getShardedJedisPool();
        ShardedJedis jedis = shardedJedisPool.getResource();

        String ccSN = uePacket.getCcSn();

        try {

            if (uePacket.getParams().length > 2 && !StringUtils.isEmpty(uePacket.getParams()[2])) {

                jedis.set(RedisPrefixConstants.EBIKE_DCU_HARDWARE + ccSN, uePacket.getParams()[2]);
            }

            if (uePacket.getParams().length > 3 && !StringUtils.isEmpty(uePacket.getParams()[3])) {

                jedis.set(RedisPrefixConstants.EBIKE_DCU_SOFTWARE + ccSN, uePacket.getParams()[3]);
            }

            jedis.set(RedisPrefixConstants.EBIKE_IMSI_PREFIX + ccSN, uePacket.getParams()[0]);

        } finally {
            shardedJedisPool.returnResource(jedis);
        }
    }
}
