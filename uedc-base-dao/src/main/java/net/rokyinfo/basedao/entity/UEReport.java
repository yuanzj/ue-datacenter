package net.rokyinfo.basedao.entity;

/**
 * Created by Administrator on 2016/8/25.
 */
public class UEReport extends Pojo {

    private static final long serialVersionUID = 8472360885496198887L;

    private String ueSn;

    private String ccSn;

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
     * gps gsm bat
     */
    private String gpsGsmBat;

    /**
     * 经纬度标示：1：东经，北纬 2：东经，南纬 3：西经，北纬 4：西经，南纬 0：无数据
     */

    private int lonLatFlag;

    /**
     * 行驶里程
     */
    private String travelMiles;

    /**
     * 电池剩余电量
     */
    private String remainCapacity;

    /**
     * 电池电压
     */
    private String voltage;

    /**
     * 电池电流
     */
    private String current;

    /**
     * 故障信息
     */
    private String carFault;

    /**
     * 车辆状态 00：设防 01：撤防 02：驻车 03：骑行 F1：外电在RS485失联 F2：外电不在
     */
    private String carStatus;

    /**
     * Mobile Country Code，移动国家代码（中国的为460）
     */
    private int mcc;

    /**
     * Mobile Network Code，移动网络号码（中国移动为00，中国联通为01）
     */
    private int mnc;

    /**
     * Location Area Code，位置区域码
     */
    private int lac;

    /**
     * Cell Identity，基站编号，是个16位的数据（范围是0到65535）
     */
    private int cid;

    /**
     * 告警
     */
    private String warn;

    /**
     * 骑行状态 1：开始一段骑行 2：结束一段骑行 0：其它
     */
    private int travelStatus;

    public String getUeSn() {
        return ueSn;
    }

    public void setUeSn(String ueSn) {
        this.ueSn = ueSn;
    }

    public String getCcSn() {
        return ccSn;
    }

    public void setCcSn(String ccSn) {
        this.ccSn = ccSn;
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

    public String getTravelMiles() {
        return travelMiles;
    }

    public void setTravelMiles(String travelMiles) {
        this.travelMiles = travelMiles;
    }

    public String getRemainCapacity() {
        return remainCapacity;
    }

    public void setRemainCapacity(String remainCapacity) {
        this.remainCapacity = remainCapacity;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getCarFault() {
        return carFault;
    }

    public void setCarFault(String carFault) {
        this.carFault = carFault;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public int getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(int travelStatus) {
        this.travelStatus = travelStatus;
    }
}
