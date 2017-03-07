package net.rokyinfo.receive.bean;


import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务器和设备交互报文基类
 *
 * @author Administrator
 */
public abstract class BasePacket {

    /**
     * 报文发送的时间
     */
    private Date time;

    /**
     * 报文类型 （T1，T2， S1等）
     */
    private String packetType;

    /**
     * 参数
     */
    private String[] params;

    /**
     * 参数开始的位置
     */
    private int paramStartPos = 6;

    /**
     * 报文解析是否正确
     */
    private boolean isMessageCorrect;

    /**
     * 终端序列号
     */
    private String ueSn;

    /**
     * 中控序列号
     */
    private String ccSn;

    /**
     * imei
     */
    private String imei;

    public BasePacket(String message, int paramStartPos) {

        try {
            if (message.startsWith("[")) {
                message = message.substring(1);
            }
            if (message.endsWith("]")) {
                message = message.substring(0, message.length() - 1);
            }
            this.paramStartPos = paramStartPos;
            String[] requestArray = message.split(",", -1);
            int arrLen = requestArray.length;

            // 设置报文类型
            this.setPacketType(requestArray[paramStartPos - 1]);

            // 请求时间
            if (!StringUtils.isEmpty(requestArray[0])) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = calcPacketTime(sdf.parse(requestArray[0]));
                this.setTime(date);
            } else {
                this.setTime(new Date());
            }

            this.params = new String[arrLen - paramStartPos];

            // 取得参数
            for (int i = 0; i < arrLen - paramStartPos; i++) {

                if (i == arrLen - paramStartPos - 1) {

                    String last = requestArray[i + paramStartPos];

                    if (last.indexOf("]") != -1) {

                        this.getParams()[i] = last.substring(0, last.length() - 1);
                    } else {

                        this.getParams()[i] = last;
                    }

                } else {

                    this.getParams()[i] = requestArray[i + paramStartPos];
                }
            }
        } catch (Exception e) {

            this.setMessageCorrect(false);
            return;
        }
    }

    public abstract Date calcPacketTime(Date originalReceivedDate);

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPacketType() {
        return packetType;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

    public String[] getParams() {
        return params;
    }

    public boolean isMessageCorrect() {
        return isMessageCorrect;
    }

    public void setMessageCorrect(boolean isMessageCorrect) {
        this.isMessageCorrect = isMessageCorrect;
    }

    public int getParamStartPos() {
        return paramStartPos;
    }

    public void setParamStartPos(int paramStartPos) {
        this.paramStartPos = paramStartPos;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
