package net.rokyinfo.basecommon.constant;

/**
 * Created by Administrator on 2016/8/12.
 */
public class RedisPrefixConstants {

    public static final String FIND_PASS_PREFIX = "find_pass:";

    public static final String REGISTE_ACCOUNT_PREFIX = "regist_account:";

    public static final String ALL_ONLINE_UE = "all_online_ue:";

    public static final String ONLINE_RK610_UE = "online_rk610_ue:";

    public static final String CAR_STATUS = "car_status:";

    public static final String CAR_STATUS_SET = "car_status_set:";

    public static final String START_SERVICE_PREFIX = "RK600:startService";

    public static final String OVER_SPEED_WARN = "overspeed_warn:";

    public static final String CHANGE_MOBILE_PREFIX = "change_mobile_prefix:";

    public static final String SERVICE_OPEN_STATUS_PREFIX = "service_open_status_prefix:";

    /** 此集合里的sn表示该车辆开始了一段行程，但是还没有结束。如果行程结束，则删除此集合中的对应sn */
    public static final String TRAVEL_START_SN = "set_travel_start_sn";

    /** 上报字段，redis中存在此key，则说明该设备最近一段时间内上报过数据 */
    public static final String UPLOAD_SN = "upload_sn:";

    /** 租车预约判断前缀 */
    public static final String RENTAL_CARS_BOOK = "rental_cars_book:";

    /** 租车预约判断用户前缀 */
    public static final String RENTAL_CARS_BOOK_USER = "rental_cars_book_user:";




    public static final String EBIKE_REPORT_PREFIX = "ebike_report:";

    public static final String EBIKE_UNSEND_SNS = "ebike_unsend_sns";

    public static final String EBIKE_IMSI_PREFIX = "ebike_imsi:";

    public static final String EBIKE_DCU_SOFTWARE = "ebike_dcu_software:";

    public static final String EBIKE_DCU_HARDWARE = "ebike_dcu_hardware";
}
