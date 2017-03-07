package net.rokyinfo.receive.redis;

import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UELogin;
import net.rokyinfo.basedao.entity.UEReport;
import net.rokyinfo.receive.bean.UEPacket;
import org.apache.log4j.Logger;

public class UpdateRedisFromLocalMemory implements UpdateUEStatusStrategy {

    private static UpdateRedisFromLocalMemory instance;

    private static Logger logger = Logger.getLogger(UpdateRedisFromLocalMemory.class);

    private UpdateRedisFromLocalMemory() {

    }

    public static UpdateRedisFromLocalMemory getUpdateRedisFromLocalMemory() {

        if (instance == null) {

            synchronized (UpdateRedisFromLocalMemory.class) {

                if (instance == null) {

                    instance = new UpdateRedisFromLocalMemory();
                }
            }
        }

        return instance;
    }

    public void updateRedis(UEPacket uePacket) {

        ObjectUpdateToRedis objectUpdateToRedis = UpdateRedisTimerTask
                .getUpdateRedisTimerTask().getObjectUpdateToRedis(uePacket.getUeSn());

        Pojo pojo = uePacket.getPojo();

        if (pojo instanceof UELogin) {

            logger.info("设备" + uePacket.getUeSn() + "的login信息准备更新到redis");
            objectUpdateToRedis.setUeLogins((UELogin) pojo);
        } else if (pojo instanceof UEReport) {

            logger.info("设备" + uePacket.getUeSn() + "的location信息准备更新到redis");
            objectUpdateToRedis.setUeReport((UEReport) pojo);
        } /**else if (pojo instanceof UEServerMsgInfo) {

            logger.info("设备" + uePacket.getUeSn() + "的UEServerMsgInfo信息准备更新到redis");
            objectUpdateToRedis.setUeServerMsgInfo((UEServerMsgInfo) pojo);
        } */else {

            return;
        }
    }

}
