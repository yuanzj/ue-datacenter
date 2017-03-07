package net.rokyinfo.basedao.entity;

/**
 * Created by Administrator on 2017-02-24.
 */
public class FirmExchangeUser extends Pojo {

    private static final long serialVersionUID = -8111534264308085261L;

    private UEFirm ueFirm;

    private String firmUser;

    private String firmPassword;

    /** 是否全量的获取报文 0非全量，只获取本企业的设备的报文数据 1全量 获取所有设备的报文数据 */
    private int allSn;

    public UEFirm getUeFirm() {
        return ueFirm;
    }

    public void setUeFirm(UEFirm ueFirm) {
        this.ueFirm = ueFirm;
    }

    public String getFirmUser() {
        return firmUser;
    }

    public void setFirmUser(String firmUser) {
        this.firmUser = firmUser;
    }

    public String getFirmPassword() {
        return firmPassword;
    }

    public void setFirmPassword(String firmPassword) {
        this.firmPassword = firmPassword;
    }

    public int getAllSn() {
        return allSn;
    }

    public void setAllSn(int allSn) {
        this.allSn = allSn;
    }
}
