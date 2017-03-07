package net.rokyinfo.basedao.dao;

import net.rokyinfo.basedao.entity.UERK600;
import net.rokyinfo.basedao.mapper.UERK600Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
@Repository("ueRK600Dao")
public class UERK600Dao {

    @Autowired
    private UERK600Mapper uerk600Mapper;

    public UERK600 getUERK600BySn(String ueSn) {

        return uerk600Mapper.getUERK600BySn(ueSn);
    }

    public void relateCC(long ccId, long rk600Id) {

        uerk600Mapper.relateCC(ccId, rk600Id);
    }

    public void updateSimInfo(long rk600Id, long simId) {

        uerk600Mapper.updateSimInfo(rk600Id, simId);
    }

    public UERK600 getUERK600ByIMEI(String imei) {

        return uerk600Mapper.getUERK600ByIMEI(imei);
    }

    public void clearCC(long ccId) {

        uerk600Mapper.clearCC(ccId);
    }

    public UERK600 getUERK600ByCCId(long ccId) {

        List<UERK600> list = uerk600Mapper.getUERK600ByCCId(ccId);
        if (list != null && list.size() > 0)
            return list.get(0);

        return null;
    }
}
