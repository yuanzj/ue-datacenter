package net.rokyinfo.basedao.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/15.
 */
public class UESim extends Pojo {

    private static final long serialVersionUID = 8999345400144620262L;

    /**
     * sim卡的号码
     */
    private long phoneNumber;

    /**
     * ICCID(Integrate circuit card identity) 固化在SIM卡中
     */
    private String iccid;

    /**
     * IMSI(International Mobile SubscriberIdentification Number) 储存在SIM卡中
     */
    private String imsi;

    private int dataPlan;

    private float remainingFlow;

    /**
     * 账户余额
     */
    private double balance;

    /**
     * 是否在移动激活 0 未激活 1 激活中 2 已激活
     */
    private int isBind;

    private Date validDate;

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getDataPlan() {
        return dataPlan;
    }

    public void setDataPlan(int dataPlan) {
        this.dataPlan = dataPlan;
    }

    public float getRemainingFlow() {
        return remainingFlow;
    }

    public void setRemainingFlow(float remainingFlow) {
        this.remainingFlow = remainingFlow;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }
}
