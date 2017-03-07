package net.rokyinfo.receive.redis;


import net.rokyinfo.basecommon.config.Config;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.dao.UEBasicInfoDao;
import net.rokyinfo.basedao.entity.UELogin;
import net.rokyinfo.basedao.entity.UEReport;
import net.rokyinfo.basedao.entity.redis.BatteryStatusInRedis;
import net.rokyinfo.basedao.entity.redis.CarStatusInRedis;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateRedisTimerTask extends TimerTask {

    private ConcurrentHashMap<String, ObjectUpdateToRedis> map;

    private UEBasicInfoDao ueBasicInfoDao;

    private ShardedJedisPool shardedJedisPool;

    private static UpdateRedisTimerTask instance;

    private int onlineUEExpired = 0;

    private static Logger logger = Logger.getLogger(UpdateRedisTimerTask.class);

    private UpdateRedisTimerTask() {

        ueBasicInfoDao = (UEBasicInfoDao) SpringContainer.getSpringContainer()
                .getBean("ueBasicInfoDao");

        shardedJedisPool = SpringContainer.getSpringContainer()
                .getShardedJedisPool();

        map = new ConcurrentHashMap<String, ObjectUpdateToRedis>();

        this.onlineUEExpired = Integer.parseInt(Config.getConfig().getParameter("ONLINE_UE_EXPIRED"));
    }

    public static UpdateRedisTimerTask getUpdateRedisTimerTask() {

        if (instance == null) {

            synchronized (UpdateRedisTimerTask.class) {

                if (instance == null) {

                    instance = new UpdateRedisTimerTask();
                }
            }
        }

        return instance;
    }

    public ObjectUpdateToRedis getObjectUpdateToRedis(String ueSn) {

        ObjectUpdateToRedis objectUpdateToRedis = this.map.get(ueSn);

        synchronized (UpdateRedisTimerTask.class) {
            if (objectUpdateToRedis == null) {

                objectUpdateToRedis = new ObjectUpdateToRedis();
                this.map.put(ueSn, objectUpdateToRedis);
            }

            return objectUpdateToRedis;
        }
    }

    public void addObjectUpdateToRedis(String ueSn,
                                       ObjectUpdateToRedis objectUpdateToRedis) {

        this.map.put(ueSn, objectUpdateToRedis);
    }

    @Override
    public void run() {

        // 将消息存入redis
        ShardedJedis jedis = this.shardedJedisPool.getResource();

        try {





            this.map.forEach((ueSn, v) -> {

                ObjectUpdateToRedis objectUpdateToRedis = this.map.get(ueSn);

                if (objectUpdateToRedis == null) {

                    return;
                }

                UELogin ueLogin = objectUpdateToRedis.getUeLogins();
                UEReport ueReport = objectUpdateToRedis.getUeReport();

                CarStatusInRedis carStatusInRedis = objectUpdateToRedis.getCarStatusInRedis();
                BatteryStatusInRedis batteryStatusInRedis = objectUpdateToRedis.getBatteryStatusInRedis();

                if (carStatusInRedis == null && batteryStatusInRedis == null) {
                    return;
                }

                if (ueLogin == null && ueReport == null) {

                    return;
                }

//                byte[] key = (CacheConst.UE_STATUS + ueSn).getBytes();

                logger.info("设置设备：" + ueSn + "状态为在线");

                jedis.setex("online_rk610_ue:" + ueSn, this.onlineUEExpired, "0");

//                Date latestLocationTime = DateUtils.getLatestDate(
//                        ueHeart == null ? null : ueHeart.getCreateTime(),
//                        ueLocationReports == null ? null : ueLocationReports.getCreateTime());
//				if (latestLocationTime != null) {
//
//					ueStatus.setLocationTime(DateUtils.format(latestLocationTime, "yyyy-MM-dd HH:mm:ss"));
//				}

//				if (ueLocationReports != null) {
//
//					if (ueLocationReports.getStatus() == 1 || ueLocationReports.getStatus() == 2) {
//
//						ueStatus.setStatus(ueLocationReports.getStatus());
//					}
//					if (ueLocationReports.getLon() != 0 && ueLocationReports.getLat() != 0) {
//
//						ueStatus.setUeLocationReport(ueLocationReports);
//					}
//				}

//				if (ueServerMsgInfo != null) {
//
//					if ("1".equals(ueServerMsgInfo.getParam1())) {
//
//						if (PacketTypeConst.T_DEFENCES.equals(ueServerMsgInfo.getMessageType())) {
//
//							ueStatus.setStatus(1);
//						} else if (PacketTypeConst.T_UNDEFENCES.equals(ueServerMsgInfo.getMessageType())) {
//
//							ueStatus.setStatus(2);
//						}
//					}
//				}

//				logger.info("更新 " + ueSn + "的redis状态");
//				logger.info("设备在线状态为：" + ueStatus.getIsOnline());
//				jedis.set(key, SerializeUtil.serialize(ueStatus));
//
//				// 获取一段时间内在线在线GPS设备的信息
//				if (jedis.hexists("gps_map_key:".getBytes(), ueSn.getBytes())) {
//					jedis.hdel("gps_map_key:".getBytes(), ueSn.getBytes());
//				}
//				jedis.hset("gps_map_key:".getBytes(), ueSn.getBytes(), SerializeUtil.serialize(ueStatus));

//				this.map.remove(ueSn);
//				objectUpdateToRedis = null;
                objectUpdateToRedis.setUeReport(null);
                objectUpdateToRedis.setUeLogins(null);
            });
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("更新redis失败，失败原因为：" + e.getMessage());
        } finally {

            shardedJedisPool.returnResource(jedis);
        }
    }
}
