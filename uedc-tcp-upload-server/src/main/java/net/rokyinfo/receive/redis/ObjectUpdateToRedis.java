package net.rokyinfo.receive.redis;


import net.rokyinfo.basedao.entity.UELogin;
import net.rokyinfo.basedao.entity.UEReport;
import net.rokyinfo.basedao.entity.redis.BatteryStatusInRedis;
import net.rokyinfo.basedao.entity.redis.CarStatusInRedis;

public class ObjectUpdateToRedis {
	
	private UELogin ueLogins;
	
	private UEReport ueReport;
	
	private BatteryStatusInRedis batteryStatusInRedis;

    private CarStatusInRedis carStatusInRedis;

    public UELogin getUeLogins() {
        return ueLogins;
    }

    public void setUeLogins(UELogin ueLogins) {
        this.ueLogins = ueLogins;
    }

    public UEReport getUeReport() {
        return ueReport;
    }

    public void setUeReport(UEReport ueReport) {
        this.ueReport = ueReport;
    }

    public BatteryStatusInRedis getBatteryStatusInRedis() {
        return batteryStatusInRedis;
    }

    public void setBatteryStatusInRedis(BatteryStatusInRedis batteryStatusInRedis) {
        this.batteryStatusInRedis = batteryStatusInRedis;
    }

    public CarStatusInRedis getCarStatusInRedis() {
        return carStatusInRedis;
    }

    public void setCarStatusInRedis(CarStatusInRedis carStatusInRedis) {
        this.carStatusInRedis = carStatusInRedis;
    }
}
