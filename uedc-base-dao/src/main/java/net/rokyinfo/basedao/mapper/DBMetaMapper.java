package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.DBMeta;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public interface DBMetaMapper {

    @Select("select * from t_ue_db_meta where id=#{id}")
    @ResultMap("dbMetaResultMap")
    DBMeta getDBMetaById(long id);

    @Select("select * from t_ue_db_meta")
    @ResultMap("dbMetaResultMap")
    List<DBMeta> getAllDB();
}
