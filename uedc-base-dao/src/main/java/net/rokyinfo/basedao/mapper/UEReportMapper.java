package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UEReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface UEReportMapper {

    public void addUEReport(UEReport ueReport);

    List<UEReport> getDayReports(@Param("ueSn") String ueSn, @Param("date") Date date);

    List<UEReport> getAllDayReports(Date date);

    List<UEReport> getAllDayReportsWithZero(Date date);

    List<UEReport> getUEReport(String ueSn);

    List<UEReport> getUEReportWithZero(String ueSn);

    List<UEReport> getTimeUEReport(@Param("ueSn") String ueSn, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<UEReport> getTimeUEReportCCSn(@Param("ccSn") String ccSn, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<UEReport> getUEReportWithZeroCCSn(@Param("ccSn") String ccSn, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<UEReport> getDayReportsWithZero(@Param("ueSn") String ueSn, @Param("date") Date date);

    List<UEReport> getDayReportsWithZeroCCSn(@Param("ccSn") String ueSn, @Param("date") Date date);

    void updateTravelStatus(@Param("reportId") long reportId, @Param("travelStatus") int travelStatus);

    void batchInsertReport(List<Pojo> list);
}
