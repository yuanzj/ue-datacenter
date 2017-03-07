package net.rokyinfo.basedao.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/17.
 */
public class UERK600 extends Pojo {

    private static final long serialVersionUID = 8340518856322217877L;

    private String ueSn;

    /** 设备类型 5：RK600 6：RK610 */
    private int ueType;

    private UESim sim;

    private UEBasicInfo ueBasicInfo;

    private UEFirm firm;

    private DBMeta dbMeta;

    private Date exportTime;

    private Date activateTime;

    /** 生命周期状态 0：正常， 1：退库     默认为0 */
    private int lifeStatus;

    private String imei;

    public String getUeSn() {
        return ueSn;
    }

    public void setUeSn(String ueSn) {
        this.ueSn = ueSn;
    }

    public UESim getSim() {
        return sim;
    }

    public void setSim(UESim sim) {
        this.sim = sim;
    }

    public UEBasicInfo getUeBasicInfo() {
        return ueBasicInfo;
    }

    public void setUeBasicInfo(UEBasicInfo ueBasicInfo) {
        this.ueBasicInfo = ueBasicInfo;
    }

    public UEFirm getFirm() {
        return firm;
    }

    public void setFirm(UEFirm firm) {
        this.firm = firm;
    }

    public DBMeta getDbMeta() {
        return dbMeta;
    }

    public void setDbMeta(DBMeta dbMeta) {
        this.dbMeta = dbMeta;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    public int getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(int lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public int getUeType() {
        return ueType;
    }

    public void setUeType(int ueType) {
        this.ueType = ueType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
