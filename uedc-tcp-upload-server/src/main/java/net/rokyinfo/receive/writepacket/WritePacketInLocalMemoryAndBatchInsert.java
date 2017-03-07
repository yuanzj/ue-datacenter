package net.rokyinfo.receive.writepacket;


import net.rokyinfo.basecommon.config.Config;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.dao.UERK600Dao;
import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UERK600;
import net.rokyinfo.receive.bean.BasePacket;
import net.rokyinfo.receive.bean.UEPacket;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WritePacketInLocalMemoryAndBatchInsert extends UEWritePacketStrategy {

    private static Map<String, List<Pojo>> allPackets = new ConcurrentHashMap<String, List<Pojo>>();

    private volatile static WritePacketInLocalMemoryAndBatchInsert instance;

    private ExecutorService pool;

    private int writeDBThreshold = 0;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(WritePacketInLocalMemoryAndBatchInsert.class);

    private WritePacketInLocalMemoryAndBatchInsert() {

        pool = Executors.newCachedThreadPool();

        this.writeDBThreshold = Integer.parseInt(Config.getConfig().getParameter("WRITE_DB_THRESHOLD"));
    }

    public static WritePacketInLocalMemoryAndBatchInsert getWritePacketInLocalMemory() {

        if (instance == null) {

            synchronized (WritePacketInLocalMemoryAndBatchInsert.class) {

                if (instance == null) {

                    instance = new WritePacketInLocalMemoryAndBatchInsert();
                }
            }
        }

        return instance;
    }

    @Override
    protected void doWrite(UEPacket uePacket) {

        if (uePacket == null) {

            return;
        }

        String cacheKey = getCacheKey(uePacket);
        List<Pojo> list = null;

        synchronized (allPackets) {

            list = allPackets.get(cacheKey);

            if (list == null) {

                list = new ArrayList<Pojo>();
                allPackets.put(cacheKey, (list));
            }

            if (list.size() >= this.writeDBThreshold) {

                List<Pojo> listToDB = new ArrayList<Pojo>();

                // 将list中的数据刷到db中，此处需要注意进行同步
                listToDB.addAll(list);
                list.clear();

                pool.execute(new WriteListToDBThread(listToDB));
            }

            list.add(uePacket.getPojo());
        }
    }

    /**
     * 此方法用于将缓存中的数据全部flush到数据库中,仅仅在TCP Server stop的时候才能调用
     */
    public void flushAllDataToDb() {

        Iterator<String> it = allPackets.keySet().iterator();

        List<Pojo> list = null;

        while (it.hasNext()) {

            String key = it.next();
            synchronized (allPackets) {

                list = allPackets.get(key);
                List<Pojo> listToDB = new ArrayList<Pojo>();

                // 将list中的数据刷到db中，此处需要注意进行同步
                listToDB.addAll(list);
                list.clear();

                // 直接写数据库
                new WriteListToDBThread(listToDB).run();

                logger.info("flush " + key + " 的数据到数据库中完成");
            }
        }

        logger.info("flush all data to db end");
    }

    public void flushT3ToDB(String ueSn) {

        String cacheKey = getT3CacheKeyFromSn(ueSn);

        List<Pojo> list = null;

        synchronized (allPackets) {

            list = allPackets.get(cacheKey);

            if (list == null) {

                return;
            }

            List<Pojo> listToDB = new ArrayList<Pojo>();

            // 将list中的数据刷到db中，此处需要注意进行同步
            listToDB.addAll(list);
            list.clear();

            // 直接写数据库
            new WriteListToDBThread(listToDB).run();
        }
    }

    private String getT3CacheKeyFromSn(String ueSn) {

        String key = WritePacketConst.UE_LOCATION;

        UERK600Dao ueRK600Dao = (UERK600Dao) SpringContainer
                .getSpringContainer().getApplicationContext()
                .getBean("ueRK600Dao");

        UERK600 ueRK600 = ueRK600Dao.getUERK600BySn(ueSn);
        return ueRK600.getDbMeta().getDbKey() + ":" + key;
    }

    /**
     * 返回数据在内存中的key
     * T3,T21,T8,T10报文存放在
     *
     * @param basePacket
     * @return
     */
    private String getCacheKey(BasePacket basePacket) {

        String key = "";
        if ("T3".equals(basePacket.getPacketType())) {

            key = WritePacketConst.UE_LOCATION;
        } else if ("T1".equals(basePacket.getPacketType())) {

            key = WritePacketConst.UE_LOGIN;
        }

        if (basePacket instanceof UEPacket) {

            UEPacket uePacket = (UEPacket) basePacket;

            UERK600Dao ueRK600Dao = (UERK600Dao) SpringContainer
                    .getSpringContainer().getApplicationContext()
                    .getBean("ueRK600Dao");

            UERK600 ueRK600 = ueRK600Dao.getUERK600BySn(uePacket.getUeSn());
            return ueRK600.getDbMeta().getDbKey() + ":" + key;

        } else {

            return key;
        }
    }
}
