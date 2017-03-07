package net.rokyinfo.receive.bean;

public class PacketTypeConst {

    /**
     * 终端上报报文类型
     */
    // T1：登陆报文
    public static final String T_LOGIN = "T1";

    // T2：参数设置请求应答报文
    public static final String T_SETUP_PARAM = "T2";

    // T3：位置信息报文类	型 客户端
    public static final String T_LOCATION = "T3";

    // T14：终端上报参数
    public static final String T_LOOKUP_PARAM = "T14";

    /**
     * 服务器下发报文类型
     */
    // S1：心跳包报文类型
    public static final String S_LOGIN = "S1";

    // S2：参数设置，有参数
    public static final String S_SETUP_PARAM = "S2";

    // S3：位置信息文类型
    public static final String S_LOCATION = "S3";

    // S11：服务器主动发起重启
    public static final String S_RESTART = "S11";

    // S14：参数查询，有参数
    public static final String S_LOOKUP_PARAM = "S14";

}
