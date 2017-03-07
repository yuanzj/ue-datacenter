package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UELogin;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface UELoginMapper {

    public void addUELogin(UELogin ueLogin);

    void batchInsertLogin(List<Pojo> list);
}
