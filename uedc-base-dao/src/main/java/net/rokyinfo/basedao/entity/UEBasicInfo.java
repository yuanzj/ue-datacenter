package net.rokyinfo.basedao.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/1.
 */
public class UEBasicInfo extends Pojo {

    private static final long serialVersionUID = -3779240483787096003L;

    private String ueSn;

    private int ueType;

    private int status;

//    private User owner;

    /**
     * 生命周期状态 0：正常， 1：退库     默认为0
     */
    private int lifeStatus;

    private UEFirm ueFirm;

//    private VehicleType vehicleType;

    private Date activedDate;

//    private FirmwareVersion firmwareVersion;

//    private UEBlueTooth ueBlueTooth;

    public String getUeSn() {
        return ueSn;
    }

    public void setUeSn(String ueSn) {
        this.ueSn = ueSn;
    }

    public int getUeType() {
        return ueType;
    }

    public void setUeType(int ueType) {
        this.ueType = ueType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(int lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public Date getActivedDate() {
        return activedDate;
    }

    public void setActivedDate(Date activedDate) {
        this.activedDate = activedDate;
    }

//    public User getOwner() {
//        return owner;
//    }
//
//    public void setOwner(User owner) {
//        this.owner = owner;
//    }

    public UEFirm getUeFirm() {
        return ueFirm;
    }

    public void setUeFirm(UEFirm ueFirm) {
        this.ueFirm = ueFirm;
    }

//    public VehicleType getVehicleType() {
//        return vehicleType;
//    }
//
//    public void setVehicleType(VehicleType vehicleType) {
//        this.vehicleType = vehicleType;
//    }

//    public FirmwareVersion getFirmwareVersion() {
//        return firmwareVersion;
//    }
//
//    public void setFirmwareVersion(FirmwareVersion firmwareVersion) {
//        this.firmwareVersion = firmwareVersion;
//    }
//
//    public UEBlueTooth getUeBlueTooth() {
//        return ueBlueTooth;
//    }
//
//    public void setUeBlueTooth(UEBlueTooth ueBlueTooth) {
//        this.ueBlueTooth = ueBlueTooth;
//    }
}
