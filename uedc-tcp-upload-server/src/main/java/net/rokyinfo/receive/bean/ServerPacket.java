package net.rokyinfo.receive.bean;

import net.rokyinfo.basedao.entity.UEServerMsg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务器下发给设备的报文
 * <p/>
 * 报文类型： S1：参数设置 WEB:[,,,sn,S2,p1], 给终端：[,S2,p] S10:平台定位 WEB:[,,,sn,S10,],
 * 给终端:[,S10] S11:重启终端 WEB:[,,,sn,S11,], 给终端:[,S11] S12:远程设防 WEB:[,,,sn,S12,],
 * 给终端:[,S12] S13:远程撤防 WEB:[,,,sn,S13,], 给终端:[,S13] S14:参数查询 WEB:[,,,sn,S14,p1],
 * 给终端:[,S14,p1]
 * <p/>
 * 报文形式： [时间，报文类型，参数 1，参数 2，参数 3 …… ]
 *
 * @author Administrator
 */
public class ServerPacket extends BasePacket {

    /**
     * 参数开始的地方
     */
    private static final int PARAM_START_POS = 2;

    private UEServerMsg uePacketEntity;

    /**
     * @param message
     */
    public ServerPacket(String message) {

        super(message, PARAM_START_POS);
    }

    @Override
    public Date calcPacketTime(Date originalReceivedDate) {

        return originalReceivedDate;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (this.getTime() != null) {
            sb.append(df.format(this.getTime())).append(",")
                    .append(this.getPacketType())
                    .append(",");
        } else {

            sb.append(",").append(this.getPacketType()).append(",");
        }

        for (String str : this.getParams()) {

            sb.append(str).append(",");
        }

        if (this.getParams().length > 0) {

            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("]");

        return sb.toString();
    }

    public void setUePacketEntity(UEServerMsg uePacketEntity) {
        this.uePacketEntity = uePacketEntity;
    }
}
