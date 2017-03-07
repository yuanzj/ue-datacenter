package net.rokyinfo.basedao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/1.
 */
public abstract class Pojo implements Serializable {

    private long id;

    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
