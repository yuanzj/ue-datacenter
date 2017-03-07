package net.rokyinfo.basedao.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/15.
 */
public class UEFirm extends Pojo {

    private static final long serialVersionUID = -3956384954132645120L;

    /** 厂商名称 */
    private String name;

    /** 厂商网址 */
    private String url;

    /** 厂商编码 */
    private String code;

    /** 备注 */
    private String remark;

    /** 厂家名称 */
    private String companyName;

    /** 厂家logo */
    private String companyLogo;

    /** 电动车品牌名称 */
    private String brandName;

    /** 品牌logo */
    private String brandLogo;

    /** 删除标志 */
    private int deleteFlag;

    /** 地址 */
    private String address;

    /** 售后电话 */
    private String phone;

    private Date modifyTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
