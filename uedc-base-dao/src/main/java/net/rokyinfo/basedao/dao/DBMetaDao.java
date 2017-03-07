package net.rokyinfo.basedao.dao;

import net.rokyinfo.basedao.entity.DBMeta;
import net.rokyinfo.basedao.mapper.DBMetaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
@Repository("dbMetaDao")
public class DBMetaDao {

    @Autowired
    private DBMetaMapper dbMetaMapper;

    public DBMeta getDBMetaById(long id) {

        return dbMetaMapper.getDBMetaById(id);
    }

    public List<DBMeta> getAllDB() {

        return dbMetaMapper.getAllDB();
    }
}
