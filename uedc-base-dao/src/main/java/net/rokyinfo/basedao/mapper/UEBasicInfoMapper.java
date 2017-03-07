package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.UEBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface UEBasicInfoMapper {

    public UEBasicInfo getUEBasicInfoById(long id);

    public UEBasicInfo getUEBasicInfoBySn(String ueSn);

    public void updateOwner(@Param("userId") Long userId, @Param("ueId") long ueId);

    public void addUEBasicInfo(UEBasicInfo ueBasicInfo);

    List<UEBasicInfo> getAllRentalCars();

    UEBasicInfo getRentalCarBySn(String ueSn);
}
