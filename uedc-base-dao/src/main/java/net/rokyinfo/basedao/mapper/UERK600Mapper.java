package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.UERK600;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public interface UERK600Mapper {

    @Select("select * from t_ue_rk600 where ue_sn=#{ueSn}")
    @ResultMap("ueRK600ResultMap")
    UERK600 getUERK600BySn(String ueSn);

    @Update("update t_ue_rk600 set cc_id=#{ccId}, active_time=(ifnull(active_time, now())) where id = #{rk600Id}")
    void relateCC(@Param("ccId") long ccId, @Param("rk600Id") long rk600Id);

    @Update("update t_ue_rk600 set sim_id=#{simId} where id=#{rk600Id}")
    void updateSimInfo(long rk600Id, long simId);

    @Select("select * from t_ue_rk600 where imei=#{imei}")
    @ResultMap("ueRK600ResultMap")
    UERK600 getUERK600ByIMEI(String imei);

    @Update("update t_ue_rk600 set cc_id=null where cc_id = #{ccId}")
    void clearCC(long ccId);

    @Select("select * from t_ue_rk600 where cc_id=#{ccId}")
    @ResultMap("ueRK600ResultMap")
    List<UERK600> getUERK600ByCCId(long ccId);

}
