package net.rokyinfo.basedao.dao;

import net.rokyinfo.basedao.entity.UESim;
import net.rokyinfo.basedao.mapper.UESimMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/15.
 */
@Repository("simDao")
public class UESimDao {

    @Autowired
    private UESimMapper ueSimMapper;

    public UESim getUESimById(long id) {

        return ueSimMapper.getUESimById(id);
    }

    public UESim getUESimByImsi(String imsi) {

        return ueSimMapper.getUESimByImsi(imsi);
    }

    public UESim getUESimByPhoneNumber(long phoneNumber) {

        return ueSimMapper.getUESimByPhoneNumber(phoneNumber);
    }
}
