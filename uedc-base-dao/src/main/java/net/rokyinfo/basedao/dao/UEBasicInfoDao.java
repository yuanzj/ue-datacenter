package net.rokyinfo.basedao.dao;

import net.rokyinfo.basedao.entity.UEBasicInfo;
import net.rokyinfo.basedao.mapper.UEBasicInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
@Repository("ueBasicInfoDao")
public class UEBasicInfoDao {

    @Autowired
    private UEBasicInfoMapper ueBasicInfoMapper;

    public UEBasicInfo getUEBasicInfoBySn(String ueSn) {

        return ueBasicInfoMapper.getUEBasicInfoBySn(ueSn);
    }

    public void updateOwner(Long userId, long ueId) {

        ueBasicInfoMapper.updateOwner(userId, ueId);
    }

    public void addUEBasicInfo(UEBasicInfo ueBasicInfo) {

        ueBasicInfoMapper.addUEBasicInfo(ueBasicInfo);
    }

    public List<UEBasicInfo> getAllRentalCars() {

        return ueBasicInfoMapper.getAllRentalCars();
    }

    public UEBasicInfo getRentalCarBySn(String ueSn) {

        return ueBasicInfoMapper.getRentalCarBySn(ueSn);
    }
}
