package net.rokyinfo.basedao.entity.redis;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CarStatusInRedis implements Serializable {

    private static final long serialVersionUID = 4864849976567256462L;

    /**
     * 车辆状态 0：设防 1：撤防 2：驻车 3：骑行 F1: F2:
     */
    private String carStatus;

    // 外电状态 0：外电在位  1：外电不在位
    private int outElecStatus = -1;

    // 485通讯状态 0：通讯正常 1：通讯异常
    private int rs485Status = -1;

    /**
     * 行驶里程 总里程
     */
    private String travelMiles;

    /**
     * 中控SN
     */
    private String ccSN;

    /**
     * GPS tracker SN
     */
    private String ueSn;

    /**
     * 故障信息
     */
    private String carFault;

    /**
     * 是否在线，0在线，1离线
     */
    private String isOnline;

    /**
     * 最后上报数据时间
     */
    private Date lastReportTime;

    /**
     * 经度
     */
    private double lon;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 速度
     */
    private String ve;

    /**
     * 最后定位时间
     */
    private Date lastLocationTime;

    /**
     * gps gsm bat
     */
    private String gpsGsmBat;

    /**
     * 经纬度标示：1：东经，北纬 2：东经，南纬 3：西经，北纬 4：西经，南纬 0：无数据
     */
    private int lonLatFlag;

    /**
     * 开始标识，1表示行程已经开始中  2表示行程结束
     */
    private int startStatus = 0;

    /**
     * 电池状态信息
     */
    private BatteryStatusInRedis batteryStatusInRedis;

    /** 定位精确度 app上报数据时有效 */
    private String horizontalAccuracy;

    /** 定位来源 0：ue  1：app */
    private int locationType = 0;

    /** DTU硬件版本号 */
    private String dtuHardWareVersion;

    /** DTU软件版本号 */
    private String dtuSoftwareVersion;

    /** 铅酸电池总格数 */
    private String soh;

    private String warn;

    private String ccuType;

    private String ccuSoftVersion;

    private String pcuType;

    private String pcuSoftVersion;

    private String ccuFirmwareVersion;

    private String ccuHardwareVersion;

    private String ecuSn;

    private String ecuFirmwareVersion;

    private String ecuHardwareVersion;

    private String pcuSn;

    private String pcuFirmwareVersion;

    public String getCcuFirmwareVersion() {
        return ccuFirmwareVersion;
    }

    public void setCcuFirmwareVersion(String ccuFirmwareVersion) {
        this.ccuFirmwareVersion = ccuFirmwareVersion;
    }

    public String getCcuHardwareVersion() {
        return ccuHardwareVersion;
    }

    public void setCcuHardwareVersion(String ccuHardwareVersion) {
        this.ccuHardwareVersion = ccuHardwareVersion;
    }

    public String getEcuSn() {
        return ecuSn;
    }

    public void setEcuSn(String ecuSn) {
        this.ecuSn = ecuSn;
    }

    public String getEcuFirmwareVersion() {
        return ecuFirmwareVersion;
    }

    public void setEcuFirmwareVersion(String ecuFirmwareVersion) {
        this.ecuFirmwareVersion = ecuFirmwareVersion;
    }

    public String getEcuHardwareVersion() {
        return ecuHardwareVersion;
    }

    public void setEcuHardwareVersion(String ecuHardwareVersion) {
        this.ecuHardwareVersion = ecuHardwareVersion;
    }

    public String getPcuSn() {
        return pcuSn;
    }

    public void setPcuSn(String pcuSn) {
        this.pcuSn = pcuSn;
    }

    public String getPcuFirmwareVersion() {
        return pcuFirmwareVersion;
    }

    public void setPcuFirmwareVersion(String pcuFirmwareVersion) {
        this.pcuFirmwareVersion = pcuFirmwareVersion;
    }

    public String getPcuHardwareVersion() {
        return pcuHardwareVersion;
    }

    public void setPcuHardwareVersion(String pcuHardwareVersion) {
        this.pcuHardwareVersion = pcuHardwareVersion;
    }

    private String pcuHardwareVersion;

    public String getCcuType() {
        return ccuType;
    }

    public void setCcuType(String ccuType) {
        this.ccuType = ccuType;
    }

    public String getCcuSoftVersion() {
        return ccuSoftVersion;
    }

    public void setCcuSoftVersion(String ccuSoftVersion) {
        this.ccuSoftVersion = ccuSoftVersion;
    }

    public String getPcuType() {
        return pcuType;
    }

    public void setPcuType(String pcuType) {
        this.pcuType = pcuType;
    }

    public String getPcuSoftVersion() {
        return pcuSoftVersion;
    }

    public void setPcuSoftVersion(String pcuSoftVersion) {
        this.pcuSoftVersion = pcuSoftVersion;
    }

    public String getSoh() {
        return soh;
    }

    public void setSoh(String soh) {
        this.soh = soh;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public String getDtuHardWareVersion() {
        return dtuHardWareVersion;
    }

    public void setDtuHardWareVersion(String dtuHardWareVersion) {
        this.dtuHardWareVersion = dtuHardWareVersion;
    }

    public String getDtuSoftwareVersion() {
        return dtuSoftwareVersion;
    }

    public void setDtuSoftwareVersion(String dtuSoftwareVersion) {
        this.dtuSoftwareVersion = dtuSoftwareVersion;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getTravelMiles() {
        return travelMiles;
    }

    public void setTravelMiles(String travelMiles) {
        this.travelMiles = travelMiles;
    }

    public Date getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(Date lastReportTime) {
        this.lastReportTime = lastReportTime;
    }

    public BatteryStatusInRedis getBatteryStatusInRedis() {
        return batteryStatusInRedis;
    }

    public void setBatteryStatusInRedis(BatteryStatusInRedis batteryStatusInRedis) {
        this.batteryStatusInRedis = batteryStatusInRedis;
    }

    public String getCcSN() {
        return ccSN;
    }

    public void setCcSN(String ccSN) {
        this.ccSN = ccSN;
    }

    public String getUeSn() {
        return ueSn;
    }

    public void setUeSn(String ueSn) {
        this.ueSn = ueSn;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getVe() {
        return ve;
    }

    public void setVe(String ve) {
        this.ve = ve;
    }

    public String getGpsGsmBat() {
        return gpsGsmBat;
    }

    public void setGpsGsmBat(String gpsGsmBat) {
        this.gpsGsmBat = gpsGsmBat;
    }

    public int getLonLatFlag() {
        return lonLatFlag;
    }

    public void setLonLatFlag(int lonLatFlag) {
        this.lonLatFlag = lonLatFlag;
    }

    public Date getLastLocationTime() {
        return lastLocationTime;
    }

    public void setLastLocationTime(Date lastLocationTime) {
        this.lastLocationTime = lastLocationTime;
    }

    public String getCarFault() {
        return carFault;
    }

    public void setCarFault(String carFault) {
        this.carFault = carFault;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public int getOutElecStatus() {
        return outElecStatus;
    }

    public void setOutElecStatus(int outElecStatus) {
        this.outElecStatus = outElecStatus;
    }

    public int getRs485Status() {
        return rs485Status;
    }

    public void setRs485Status(int rs485Status) {
        this.rs485Status = rs485Status;
    }

    public int getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(int startStatus) {
        this.startStatus = startStatus;
    }

    public String getHorizontalAccuracy() {
        return horizontalAccuracy;
    }

    public void setHorizontalAccuracy(String horizontalAccuracy) {
        this.horizontalAccuracy = horizontalAccuracy;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }
}