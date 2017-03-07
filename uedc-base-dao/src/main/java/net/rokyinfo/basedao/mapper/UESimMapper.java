package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.UESim;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/15.
 */
public interface UESimMapper {

    UESim getUESimById(long id);

    UESim getUESimByImsi(String imsi);

    UESim getUESimByPhoneNumber(long phoneNumber);
}
