package net.rokyinfo.basedao.entity;

import net.rokyinfo.basedao.entity.Pojo;

import java.util.Date;

/**
 * @author Administrator
 */
public class UEServerMsg extends Pojo {

    private static final long serialVersionUID = 6032052521195618885L;

    private long id;

    private String ueSn;

    private String messageType;//报文类型

    private Date createTime;

    private String param1;

    private Date receiveTime;

    private String receiveContent;

    private String agreementVersion;

    public String getUeSn() {
        return ueSn;
    }

    public void setUeSn(String ueSn) {
        this.ueSn = ueSn;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveContent() {
        return receiveContent;
    }

    public void setReceiveContent(String receiveContent) {
        this.receiveContent = receiveContent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAgreementVersion() {
        return agreementVersion;
    }

    public void setAgreementVersion(String agreementVersion) {
        this.agreementVersion = agreementVersion;
    }

}
