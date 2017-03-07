package net.rokyinfo.basedao.dao;

import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UELogin;
import net.rokyinfo.basedao.mapper.UELoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository("ueLoginDao")
public class UELoginDao {

    @Autowired
    private UELoginMapper ueLoginMapper;

    public void addUELogin(UELogin ueLogin) {

        ueLoginMapper.addUELogin(ueLogin);
    }

    public void batchInsertLogin(List<Pojo> list) {

        ueLoginMapper.batchInsertLogin(list);
    }
}
