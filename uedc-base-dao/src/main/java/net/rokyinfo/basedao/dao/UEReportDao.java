package net.rokyinfo.basedao.dao;

import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UEReport;
import net.rokyinfo.basedao.mapper.UEReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository("ueReportDao")
public class UEReportDao {

    @Autowired
    private UEReportMapper ueReportMapper;

    public void addUEReport(UEReport ueReport) {

        ueReportMapper.addUEReport(ueReport);
    }

    public List<UEReport> getDayReports(String ueSn, Date date) {

        return ueReportMapper.getDayReports(ueSn, date);
    }

    public List<UEReport> getDayReportsWithZero(String ueSn, Date date) {

        return ueReportMapper.getDayReportsWithZero(ueSn, date);
    }

    public List<UEReport> getDayReportsWithZeroCCSn(String ccSn, Date date) {

        return ueReportMapper.getDayReportsWithZeroCCSn(ccSn, date);
    }

    public List<UEReport> getAllDayReports(Date date) {

        return ueReportMapper.getAllDayReports(date);
    }

    public List<UEReport> getAllDayReportsWithZero(Date date) {

        return ueReportMapper.getAllDayReportsWithZero(date);
    }

    public List<UEReport> getUEReport(String ueSn) {

        return ueReportMapper.getUEReport(ueSn);
    }

    public List<UEReport> getUEReportWithZero(String ueSn) {

        return ueReportMapper.getUEReportWithZero(ueSn);
    }

    public List<UEReport> getTimeUEReport(String ueSn, Date startTime, Date endTime) {

        return ueReportMapper.getTimeUEReport(ueSn, startTime, endTime);
    }

    public List<UEReport> getTimeUEReportCCSn(String ccSn, Date startTime, Date endTime) {

        return ueReportMapper.getTimeUEReportCCSn(ccSn, startTime, endTime);
    }

    public List<UEReport> getUEReportWithZeroCCSn(String ccSn, Date startTime, Date endTime) {

        return ueReportMapper.getUEReportWithZeroCCSn(ccSn, startTime, endTime);
    }

    public void updateTravelStatus(long reportId, int travelStatus) {

        ueReportMapper.updateTravelStatus(reportId, travelStatus);
    }

    public void batchInsertReport(List<Pojo> list) {

        ueReportMapper.batchInsertReport(list);
    }
}
