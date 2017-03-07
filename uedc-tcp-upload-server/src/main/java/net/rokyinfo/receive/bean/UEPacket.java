package net.rokyinfo.receive.bean;

import net.rokyinfo.basedao.entity.Pojo;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设备上报上来的报文
 * <p/>
 * 报文类型：
 * T0：心跳包
 * T1：登录
 * T2：应答设置请求
 * T3：状态实时上报
 * T4：主电断开告警
 * T7：后备电池电量不足告警
 * T8：越界告警
 * T10：定位信息上报应答
 * T12：远程设防回应
 * T13：远程撤防回应
 * T14：终端上报参数
 * T21：震动告警
 * <p/>
 * 报文形式：
 * [时间，终端类型,协议版本号,终端序列号，中控序列号，报文类型，参数 1，参数 2，参数 ，参数 3 …… ]
 *
 * @author Administrator
 */
public class UEPacket extends BasePacket {

    /**
     * 终端类型 0:电动车
     */
    private String ueType = "0";

    /**
     * 协议版本号，默认R1
     */
    private String protocolVersion = "R1";

    /**
     * 参数开始的地方
     */
    private static final int PARAM_START_POS = 6;

    private Pojo pojo;

    /**
     * @param message
     */
    public UEPacket(String message) {

        super(message, PARAM_START_POS);

        try {
            String[] requestArray = message.split(",", -1);

            // 终端类型
            this.setUeType(requestArray[1]);

            // 协议版本
            this.setProtocolVersion(requestArray[2]);

            // imei
            this.setImei(requestArray[3]);

            if (StringUtils.isEmpty(this.getImei())) {
                this.setMessageCorrect(false);
                return;
            }

            // ccsn
            this.setCcSn(requestArray[4]);

            // 取得上报消息类型
            int mtype = Integer.parseInt(requestArray[this.getParamStartPos() - 1].substring(1));
            if (mtype != 1 && mtype != 2 && mtype != 3 && mtype != 14) {

                this.setMessageCorrect(false);
                return;
            }

            this.setMessageCorrect(true);

        } catch (Exception e) {

            this.setMessageCorrect(false);
            return;
        }
    }

    /**
     * 重新toString
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (this.getTime() != null) {

            sb.append(df.format(this.getTime())).append(",");
        } else {

            sb.append(",");
        }
        if (this.getUeType() != null) {

            sb.append(this.getUeType()).append(",");
        } else {

            sb.append(",");
        }
        if (this.getProtocolVersion() != null) {

            sb.append(this.getProtocolVersion()).append(",");
        } else {

            sb.append(",");
        }

        sb.append(this.getImei()).append(",");

        if (this.getCcSn() != null) {
            sb.append(this.getCcSn()).append(",");
        } else {
            sb.append(",");
        }

        sb.append(this.getPacketType()).append(",");

        for (String str : this.getParams()) {

            sb.append(str).append(",");
        }

        if (this.getParams().length > 0) {

            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public Date calcPacketTime(Date originalReceivedDate) {

        long between = 0;
        long deadline = 4 * 3600; // 4 hours

        between = (new Date().getTime() - originalReceivedDate.getTime()) / 1000;

        if (between >= deadline || between <= -deadline) {
            return new Date();
        } else {
            return originalReceivedDate;
        }
    }

    public String getUeType() {
        return ueType;
    }

    public void setUeType(String ueType) {
        this.ueType = ueType;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Pojo getPojo() {
        return pojo;
    }

    public void setPojo(Pojo pojo) {
        this.pojo = pojo;
    }
}
