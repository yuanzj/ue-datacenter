package net.rokyinfo.basedao.entity.redis;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BatteryStatusInRedis implements Serializable {

    private static final long serialVersionUID = 5531226172781267649L;

    /**
     * 电池状态
     */
    private String batteryStatus;

    /**
     * 电池剩余电量
     */
    private String remainCapacity;

    /**
     * 电池健康状况：soc + soh
     */
    private String socSoh;

    /**
     * 电池电压
     */
    private String voltage;

    /**
     * 电池电流
     */
    private String current;

    /**
     * 电池温度
     */
    private String temperature;

    /**
     * 更新时间
     */
    private Date modifyTime;

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getRemainCapacity() {
        return remainCapacity;
    }

    public void setRemainCapacity(String remainCapacity) {
        this.remainCapacity = remainCapacity;
    }

    public String getSocSoh() {
        return socSoh;
    }

    public void setSocSoh(String socSoh) {
        this.socSoh = socSoh;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
