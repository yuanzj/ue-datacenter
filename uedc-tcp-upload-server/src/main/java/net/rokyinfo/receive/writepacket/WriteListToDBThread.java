package net.rokyinfo.receive.writepacket;


import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.dao.UELoginDao;
import net.rokyinfo.basedao.dao.UEReportDao;
import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UELogin;
import net.rokyinfo.basedao.entity.UEReport;

import java.util.List;

public class WriteListToDBThread extends Thread {

    private List<Pojo> list;

    private UEReportDao ueReportDao;

    private UELoginDao ueLoginDao;

    public WriteListToDBThread(List<Pojo> list) {

        this.list = list;

        ueLoginDao = (UELoginDao) SpringContainer.getSpringContainer().getBean("ueLoginDao");
        ueReportDao = (UEReportDao) SpringContainer.getSpringContainer().getBean("ueReportDao");
    }

    @Override
    public void run() {

        if (list.size() <= 0) {
            return;
        }

        Pojo pojo = list.get(0);

        if (pojo instanceof UELogin) {

            ueLoginDao.batchInsertLogin(list);
        } else if (pojo instanceof UEReport) {

            ueReportDao.batchInsertReport(list);
        }

        this.list.clear();
        this.list = null;
    }
}
