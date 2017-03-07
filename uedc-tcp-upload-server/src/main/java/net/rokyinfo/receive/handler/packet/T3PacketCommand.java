package net.rokyinfo.receive.handler.packet;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.rokyinfo.ble.toolbox.protocol.model.RK4103Fault;
import com.rokyinfo.convert.exception.FieldConvertException;
import com.rokyinfo.convert.exception.RkFieldException;
import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.basecommon.config.Config;
import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basecommon.constant.RedisPrefixConstants;
import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UEReport;
import net.rokyinfo.model.EbikeListProtos;
import net.rokyinfo.receive.bean.PacketTypeConst;
import net.rokyinfo.receive.bean.UEPacket;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class T3PacketCommand extends net.rokyinfo.receive.handler.UEPacketCommand {

    private UEReport ueReport;

    private static final int ONLINE_UE_EXPIRED = Integer.parseInt(Config.getConfig().getParameter("ONLINE_UE_EXPIRED"));

    private static final int UPLOAD_SN_EXPIRED = Integer.parseInt(Config.getConfig().getParameter("UPLOAD_SN_EXPIRED"));

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(T3PacketCommand.class);

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public T3PacketCommand() {

    }

    public T3PacketCommand(UEPacket uePacket) {

    }

    @Override
    protected void getDetailReplyToClientMsg(boolean isReplySuccess) {

        if (isReplySuccess) {

            setReplyToClientMsg("[," + PacketTypeConst.S_LOCATION + "]");
        } else {

            setReplyToClientMsg(null);
        }
    }

    @Override
    protected Pojo fromPacketToEntity(UEPacket uePacket)
            throws Exception {

        UEReport ueReport = new UEReport();
        ueReport.setUeSn(uePacket.getUeSn());
        ueReport.setCcSn(uePacket.getCcSn());

        ShardedJedisPool shardedJedisPool = SpringContainer.getSpringContainer().getShardedJedisPool();
        ShardedJedis jedis = shardedJedisPool.getResource();

        EbikeListProtos.Ebike.Builder ebikeBuilder = EbikeListProtos.Ebike.newBuilder();

        ebikeBuilder.getLbsBuilder().setSource(EbikeListProtos.DataSource.GPS_DEVICE);
        ebikeBuilder.getDcuBuilder().setIMEI(uePacket.getImei());

        ebikeBuilder.getCcuBuilder().setSN(uePacket.getCcSn());

        String dcuHardware = jedis.get(RedisPrefixConstants.EBIKE_DCU_HARDWARE + uePacket.getCcSn());
        String dcuSoftware = jedis.get(RedisPrefixConstants.EBIKE_DCU_SOFTWARE + uePacket.getCcSn());
        String imsi = jedis.get(RedisPrefixConstants.EBIKE_IMSI_PREFIX + uePacket.getCcSn());

        if (!StringUtils.isEmpty(dcuHardware)) {

            ebikeBuilder.getDcuBuilder().setHardware(dcuHardware);
        }

        if (!StringUtils.isEmpty(dcuSoftware)) {

            ebikeBuilder.getDcuBuilder().setSoftware(dcuSoftware);
        }

        if (!StringUtils.isEmpty(imsi)) {

            ebikeBuilder.getDcuBuilder().setIMSI(imsi);
        }

        if (!StringUtils.isEmpty(uePacket.getParams()[0]) && !StringUtils.isEmpty(uePacket.getParams()[0].trim())) {

            ueReport.setLonLatFlag(Integer.parseInt(uePacket.getParams()[0]));
            ebikeBuilder.getLbsBuilder().setLonLatType(EbikeListProtos.LonLatType.WEST_LONGITUDE_NORTH_LATITUDE);
        }

        ueReport.setCarFault(uePacket.getParams()[7]);
        ebikeBuilder.setFault(ByteString.copyFrom(uePacket.getParams()[7].getBytes()));

        if (!StringUtils.isEmpty(uePacket.getParams()[3])) {
            ueReport.setMcc(Integer.parseInt(uePacket.getParams()[3].split(":")[0]));
            ueReport.setMnc(Integer.parseInt(uePacket.getParams()[3].split(":")[1]));
            ueReport.setLac(Integer.parseInt(uePacket.getParams()[3].split(":")[2]));
            ueReport.setCid(Integer.parseInt(uePacket.getParams()[3].split(":")[3]));
        }

        if (uePacket.getParams().length > 13) {
            ueReport.setRemainCapacity(uePacket.getParams()[13]);

            ebikeBuilder.getBmsBuilder().setSOC(Integer.parseInt(uePacket.getParams()[13], 16));
        }

        ueReport.setCreateTime(uePacket.getTime());
        ebikeBuilder.setReportTime(df.format(uePacket.getTime()));

        try {
            if (!StringUtils.isEmpty(uePacket.getParams()[1]) && !StringUtils.isEmpty(uePacket.getParams()[1].trim())) {
                ueReport.setLon(Double.parseDouble(uePacket.getParams()[1]));
                ebikeBuilder.getLbsBuilder().setLon(Double.parseDouble(uePacket.getParams()[1]));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[2]) && !StringUtils.isEmpty(uePacket.getParams()[2].trim())) {
                ueReport.setLat(Double.parseDouble(uePacket.getParams()[2]));
                ebikeBuilder.getLbsBuilder().setLat(Double.parseDouble(uePacket.getParams()[2]));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[6]) && !StringUtils.isEmpty(uePacket.getParams()[6].trim())) {

                ueReport.setCarStatus(uePacket.getParams()[6]);
                ebikeBuilder.setStatus(Integer.parseInt(uePacket.getParams()[6], 16));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[8]) && !StringUtils.isEmpty(uePacket.getParams()[8].trim())) {

                ueReport.setVe((uePacket.getParams()[8]));
                ebikeBuilder.setSpeed(Integer.parseInt(uePacket.getParams()[8], 16));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[9]) && !StringUtils.isEmpty(uePacket.getParams()[9].trim())) {

                ueReport.setTravelMiles(uePacket.getParams()[9]);
                ebikeBuilder.setODO(Long.parseLong(uePacket.getParams()[9], 16));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[4]) && !StringUtils.isEmpty(uePacket.getParams()[4].trim())) {

                ueReport.setGpsGsmBat(uePacket.getParams()[4]);
                ebikeBuilder.getDcuBuilder().setGPSRSSI(Integer.parseInt(uePacket.getParams()[4].substring(0, 1)));
                ebikeBuilder.getDcuBuilder().setGSMRSSI(Integer.parseInt(uePacket.getParams()[4].substring(1, 2)));
                ebikeBuilder.getDcuBuilder().setVBAT(Integer.parseInt(uePacket.getParams()[4].substring(2, 3)));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[5]) && !StringUtils.isEmpty(uePacket.getParams()[5].trim())) {

                ueReport.setWarn(uePacket.getParams()[5]);
                ebikeBuilder.setAlarm(Integer.parseInt(uePacket.getParams()[5], 16));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[11]) && !StringUtils.isEmpty(uePacket.getParams()[11].trim())) {

                ueReport.setVoltage(uePacket.getParams()[11]);
                ebikeBuilder.setVoltage(Integer.parseInt(uePacket.getParams()[11], 16));
            }

            if (!StringUtils.isEmpty(uePacket.getParams()[12]) && !StringUtils.isEmpty(uePacket.getParams()[12].trim())) {

                ueReport.setCurrent(uePacket.getParams()[12]);
                ebikeBuilder.setCurrent(Integer.parseInt(uePacket.getParams()[12], 16));
            }

            if (uePacket.getParams().length > 14 && !StringUtils.isEmpty(uePacket.getParams()[14])) {

                ebikeBuilder.getBmsBuilder().setSOH((Integer.parseInt(uePacket.getParams()[14])));
            }

            jedis.set((RedisPrefixConstants.EBIKE_REPORT_PREFIX + uePacket.getCcSn()).getBytes(), ebikeBuilder.build().toByteArray());
            jedis.sadd(RedisPrefixConstants.EBIKE_UNSEND_SNS, uePacket.getCcSn());

        } finally {

            shardedJedisPool.returnResource(jedis);
        }

        this.ueReport = ueReport;

        uePacket.setPojo(ueReport);

        return ueReport;
    }

    /**
     * T3报文先写位置信息，再写电池信息
     *
     * @param uePacket
     */
    protected void writePacket(UEPacket uePacket) {

        // 先写t_ue_location_report2表
        uePacket.setPojo(this.ueReport);
        this.getWritePacketStrategy().writePacket(uePacket);
    }

    @Override
    protected void updateStatusToCache(UEPacket uePacket) {

    }

    @Override
    protected void postPacket(ChannelHandlerContext ctx, UEPacket uePacket) {

    }

    private String getFaultMessage(RK4103Fault rk4103Fault) throws IllegalAccessException, RkFieldException, FieldConvertException {

        StringBuilder sb = new StringBuilder();

        // 灯光单元故障
        List<String> lightFaultList = Lists.newArrayList();

        // 动力单元故障
        List<String> powerFaultList = Lists.newArrayList();

        // 数据单元故障
        List<String> dataFaultList = Lists.newArrayList();

        // 中控单元故障
        List<String> ccFaultList = Lists.newArrayList();

        // 电池单元故障
        List<String> batteryFaultList = Lists.newArrayList();

        // 通讯单元故障
        List<String> communicationFaultList = Lists.newArrayList();

        /**
         * 电机类
         */
        if (rk4103Fault.getMos() == 1) {
            powerFaultList.add("MOS故障");
        }
        if (rk4103Fault.getTurnedHandle() == 1) {
            powerFaultList.add("转把故障");
        }
        if (rk4103Fault.getPhase() == 1) {
            powerFaultList.add("缺相故障");
        }
        if (rk4103Fault.getHall() == 1) {
            powerFaultList.add("霍尔故障");
        }
        if (rk4103Fault.getBrakeHandle() == 1) {
            powerFaultList.add("刹把故障");
        }
        if (rk4103Fault.getElectric() == 1) {
            powerFaultList.add("电机故障");
        }
        if (rk4103Fault.getEcuCommunication() == 1) {
            communicationFaultList.add("ECU通信异常");
        }

        /**
         * 中控类
         */
        if (rk4103Fault.getGsensor() == 1) {
            ccFaultList.add("重力传感器故障");
        }
        if (rk4103Fault.getBle() == 1) {
            ccFaultList.add("蓝牙故障");
        }

        /**
         * 电池故障
         */
        if (rk4103Fault.getBmsCommunication() == 1) {
            batteryFaultList.add("BMS通信故障");
        }
        if (rk4103Fault.getOverCharge() == 1) {
            batteryFaultList.add("过充故障");
        }
        if (rk4103Fault.getElecChipHighTemperature() == 1) {
            batteryFaultList.add("电芯高温故障");
        }
        if (rk4103Fault.getElecChipLowTemperature() == 1) {
            batteryFaultList.add("电芯低温故障");
        }
        if (rk4103Fault.getBatteryUnderVoltage() == 1) {
            batteryFaultList.add("电池欠压");
        }

        /**
         * GPS故障
         */
        if (rk4103Fault.getGpsCommunication() == 1) {
            dataFaultList.add("GPS通信故障");
        }
        if (rk4103Fault.getGpsSignalAnomaly() == 1) {
            dataFaultList.add("GPS信号异常");
        }
        if (rk4103Fault.getGsmSignalAnomaly() == 1) {
            dataFaultList.add("GSM信号异常");
        }
        if (rk4103Fault.getNoSim() == 1) {
            dataFaultList.add("无SIM卡");
        }
        if (rk4103Fault.getOutElecOff() == 1) {
            dataFaultList.add("外电断开");
        }

        /**
         * 其它
         */
        if (rk4103Fault.getRs485() == 1) {
            communicationFaultList.add("RS485通信故障");
        }
        if (rk4103Fault.getRcAnomaly() == 1) {
            communicationFaultList.add("RC异常");
        }

        /**
         * PCU故障
         */
        if (rk4103Fault.getShortCircuit() == 1 || rk4103Fault.getHardwareFault() == 1) {
            batteryFaultList.add("电源管理器故障");
        }
        if (rk4103Fault.getDcEnableControl() == 1 || rk4103Fault.getDcOutputOverVoltage() == 1
                || rk4103Fault.getDcOutputUnderVoltage() == 1) {
            batteryFaultList.add("电源转换器故障");
        }
        if (rk4103Fault.getPowerManagerCommunication() == 1) {
            communicationFaultList.add("电源管理通信故障");
        }

        /**
         * 灯光断路故障1
         */
        if (rk4103Fault.getCbFrontLeftTurn() == 1) {
            lightFaultList.add("前左转灯断路故障");
        }
        if (rk4103Fault.getCbBackLeftTurn() == 1) {
            lightFaultList.add("后左转灯断路故障");
        }
        if (rk4103Fault.getCbFrontRightTurn() == 1) {
            lightFaultList.add("前右转灯断路故障");
        }
        if (rk4103Fault.getCbBackRightTurn() == 1) {
            lightFaultList.add("后右转灯断路故障");
        }
        if (rk4103Fault.getCbNearLight() == 1) {
            lightFaultList.add("近光灯断路故障");
        }
        if (rk4103Fault.getCbBeam() == 1) {
            lightFaultList.add("远光灯断路故障");
        }
        if (rk4103Fault.getCbTaillight() == 1) {
            lightFaultList.add("尾灯断路故障");
        }
        if (rk4103Fault.getCbBrakeLight() == 1) {
            lightFaultList.add("刹车灯断路故障");
        }

        /**
         * 断路故障2
         */
        if (rk4103Fault.getCbBackgroundLight1() == 1) {
            lightFaultList.add("背景灯1断路故障");
        }
        if (rk4103Fault.getCbBackgroundLight2() == 1) {
            lightFaultList.add("背景灯2断路故障");
        }
        if (rk4103Fault.getCbBackgroundLight3() == 1) {
            lightFaultList.add("背景灯3断路故障");
        }
        if (rk4103Fault.getCbHornFault() == 1) {
            lightFaultList.add("喇叭断路故障");
        }

        /**
         * 灯光短路故障1
         */
        if (rk4103Fault.getScFrontLeftTurn() == 1) {
            lightFaultList.add("前左转灯短路故障");
        }
        if (rk4103Fault.getScBackLeftTurn() == 1) {
            lightFaultList.add("后左转灯短路故障");
        }
        if (rk4103Fault.getScFrontRightTurn() == 1) {
            lightFaultList.add("前右转灯短路故障");
        }
        if (rk4103Fault.getScBackRightTurn() == 1) {
            lightFaultList.add("后右转灯短路故障");
        }
        if (rk4103Fault.getScNearLight() == 1) {
            lightFaultList.add("近光灯短路故障");
        }
        if (rk4103Fault.getScBeam() == 1) {
            lightFaultList.add("远光灯短路故障");
        }
        if (rk4103Fault.getScTaillight() == 1) {
            lightFaultList.add("尾灯短路故障");
        }
        if (rk4103Fault.getScBrakeLight() == 1) {
            lightFaultList.add("刹车灯短路故障");
        }

        /**
         * 短路故障2
         */
        if (rk4103Fault.getScBackgroundLight1() == 1) {
            lightFaultList.add("背景灯1短路故障");
        }
        if (rk4103Fault.getScBackgroundLight2() == 1) {
            lightFaultList.add("背景灯2短路故障");
        }
        if (rk4103Fault.getScBackgroundLight3() == 1) {
            lightFaultList.add("背景灯3短路故障");
        }
        if (rk4103Fault.getScHornFault() == 1) {
            lightFaultList.add("喇叭短路故障");
        }

        if (lightFaultList.size() > 0) {

            String fault = StringUtils.join(lightFaultList, "、");
            sb.append("灯光单元").append(fault).append("；");
        }

        if (powerFaultList.size() > 0) {

            String fault = StringUtils.join(powerFaultList, "、");
            sb.append("动力单元").append(fault).append("；");
        }

        if (dataFaultList.size() > 0) {

            String fault = StringUtils.join(dataFaultList, "、");
            sb.append("数据单元").append(fault).append("；");
        }

        if (ccFaultList.size() > 0) {

            String fault = StringUtils.join(ccFaultList, "、");
            sb.append("中控单元").append(fault).append("；");
        }

        if (batteryFaultList.size() > 0) {

            String fault = StringUtils.join(batteryFaultList, "、");
            sb.append("电池单元").append(fault).append("；");
        }

        if (communicationFaultList.size() > 0) {

            String fault = StringUtils.join(communicationFaultList, "、");
            sb.append("通讯单元").append(fault).append("；");
        }

        // 去掉最后的;加上！
        if (sb.length() > 2) {
            return sb.substring(0, sb.length() - 1) + "！";
        }

        return null;
    }

    public static void main(String[] args) throws IllegalAccessException, RkFieldException, FieldConvertException {

        T3PacketCommand t3PacketCommand = new T3PacketCommand();

        RK4103Fault rk4103Fault = new RK4103Fault();
        rk4103Fault = rk4103Fault.build(net.rokyinfo.basecommon.util.StringUtils.
                stringToByteArray("FFFFFFFFFFFFFFFFFFFF"));

        String faultMessage = t3PacketCommand.getFaultMessage(rk4103Fault);
        System.out.println(faultMessage);

        System.out.println(Integer.parseInt("01"));
    }
}
