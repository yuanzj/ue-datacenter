package net.rokyinfo.appcontroller.controller;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.appcontroller.annotation.MappingMethod;
import net.rokyinfo.appcontroller.bean.HttpNettyRequest;
import net.rokyinfo.appcontroller.bean.HttpResponseEntity;
import net.rokyinfo.appcontroller.util.PoolMQSender;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basecommon.constant.Constants;
import net.rokyinfo.basecommon.constant.RedisPrefixConstants;
import net.rokyinfo.basecommon.util.JSONUtils;
import net.rokyinfo.basedao.dao.UEBasicInfoDao;
import net.rokyinfo.basedao.dao.UERK600Dao;
import net.rokyinfo.basedao.entity.UEBasicInfo;
import net.rokyinfo.basedao.entity.UERK600;
import net.rokyinfo.model.EbikeListProtos;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.jms.JMSException;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017-02-23.
 */
@Controller("v1")
public class DataExchangeV1Controller extends HttpBaseController {

    @Autowired
    private UEBasicInfoDao ueBasicInfoDao;

    @Autowired
    private UERK600Dao uerk600Dao;

    @Autowired
    private PoolMQSender poolMQSender;

    @MappingMethod("receivedCache/ebikes")
    public int getReceivedCache(ChannelHandlerContext ctx, HttpNettyRequest request) {

        ShardedJedisPool shardedJedisPool = SpringContainer.getSpringContainer().getShardedJedisPool();
        ShardedJedis jedis = shardedJedisPool.getResource();

        try {
            Set<String> allUEs = jedis.smembers(RedisPrefixConstants.EBIKE_UNSEND_SNS);
            Set<String> destinationSet = Sets.newHashSet();

            long allUENum = jedis.scard(RedisPrefixConstants.EBIKE_UNSEND_SNS);

            if (StringUtils.isEmpty(request.getParameters().get("maxCount")) ||
                    allUENum < Integer.parseInt(request.getParameters().get("maxCount"))) {

                destinationSet.addAll(allUEs);

            } else {

                for (String str : allUEs) {

                    if (destinationSet.size() > Integer.parseInt(request.getParameters().get("maxCount"))) {
                        break;
                    }

                    destinationSet.add(str);
                }
            }

            String[] delSn = new String[destinationSet.size()];
            int i = 0;
            for (String str : destinationSet) {

                delSn[i++] = str;
            }

            if (delSn.length > 0) {
                // 删除redis集合中的元素
                jedis.srem(RedisPrefixConstants.EBIKE_UNSEND_SNS, delSn);
            }

            EbikeListProtos.EbikeList.Builder ebikeList = EbikeListProtos.EbikeList.newBuilder();

            for (String ueSn : destinationSet) {

                byte[] ebikeArray = jedis.get((RedisPrefixConstants.EBIKE_REPORT_PREFIX + ueSn).getBytes());
                if (ebikeArray == null || ebikeArray.length < 1) {
                    continue;
                }

                ebikeList.addEbike(EbikeListProtos.Ebike.parseFrom(ebikeArray));
            }

            writeResponseByte(ctx, ebikeList.build().toByteArray(), request);

        } catch (NumberFormatException e) {

            e.printStackTrace();
        } catch (InvalidProtocolBufferException e) {

            e.printStackTrace();
        } catch (JedisConnectionException e){

            e.printStackTrace();

        }finally {

            shardedJedisPool.returnResource(jedis);
        }

        return 0;
    }

    @MappingMethod("send/singleEbike")
    public int sendCmdToUE(ChannelHandlerContext ctx, HttpNettyRequest request) {

        String ueSn = request.getParameters().get("ueSn");
        String sendData = request.getParameters().get("data");

        HttpResponseEntity result = new HttpResponseEntity();

        if (StringUtils.isEmpty(ueSn)) {

            result.setState(1);
            result.setMessage("设备序列号为空");
            writeResponse(ctx, result, request);
            return 1;
        }

        if (StringUtils.isEmpty(sendData)) {

            result.setState(2);
            result.setMessage("发送指令为空");
            writeResponse(ctx, result, request);
            return 1;
        }

        UEBasicInfo ueBasicInfo = ueBasicInfoDao.getUEBasicInfoBySn(ueSn);

        if (ueBasicInfo == null) {

            result.setState(3);
            result.setMessage("错误的设备序列号");
            writeResponse(ctx, result, request);
            return 1;
        }

        UERK600 uerk600 = uerk600Dao.getUERK600ByCCId(ueBasicInfo.getId());
        if (uerk600 == null) {

            result.setState(4);
            result.setMessage("错误的设备序列号");
            writeResponse(ctx, result, request);
            return 1;
        }

        Map<String, String> map = Maps.newHashMap();
        map.put("sn", uerk600.getUeSn());
        map.put("userId", "0");
        map.put("packet", sendData);
        map.put("userMessageId", "1");
        map.put("hostName", "ue_service");

        try {
            poolMQSender.sendCommonMsgToTopic(map, Constants.CMD_TO_UE_QUEUE);
        } catch (JMSException e) {
            e.printStackTrace();

            result.setState(5);
            result.setMessage("发送命令失败");
            writeResponse(ctx, result, request);
            return 1;
        }

        writeResponse(ctx, result, request);
        return 0;
    }
}
