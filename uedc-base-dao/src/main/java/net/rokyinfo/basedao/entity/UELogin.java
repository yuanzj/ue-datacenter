package net.rokyinfo.basedao.entity;

/**
 * Created by Administrator on 2016/8/25.
 */
public class UELogin extends Pojo {

    private static final long serialVersionUID = 3592531677478570257L;

    private String ueSn;

    private String ccSn;

    private String imsi;

    /**
     * 车辆状态 0：设防 1：撤防 2：驻车 3：骑行 F1：外电在RS485失联 F2：外电不在
     */
    private String carStatus;

    private int loginResult;

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

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public int getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(int loginResult) {
        this.loginResult = loginResult;
    }
}
