package net.rokyinfo.basedao.entity;

/**
 * Created by Administrator on 2016/8/4.
 */
public class DBMeta extends Pojo {

    private static final long serialVersionUID = -7720200957981537556L;

    /** 数据库名称 */
    private String dbName;

    /** 数据库ip地址 */
    private String dbAddress;

    /** 数据库key */
    private String dbKey;

    /** 数据库设备记录数 */
    private long ueAmount;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(String dbAddress) {
        this.dbAddress = dbAddress;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public long getUeAmount() {
        return ueAmount;
    }

    public void setUeAmount(long ueAmount) {
        this.ueAmount = ueAmount;
    }
}
