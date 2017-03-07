package net.rokyinfo.receive.writepacket;


import net.rokyinfo.basecommon.config.SpringContainer;
import net.rokyinfo.basedao.dao.UELoginDao;
import net.rokyinfo.basedao.dao.UEReportDao;
import net.rokyinfo.basedao.entity.Pojo;
import net.rokyinfo.basedao.entity.UELogin;
import net.rokyinfo.basedao.entity.UEReport;
import net.rokyinfo.receive.bean.UEPacket;
import org.apache.commons.lang.StringUtils;

public class WritePacketDirectToDB extends UEWritePacketStrategy {

    private UELoginDao ueLoginDao;

    private UEReportDao ueReportDao;

    private static volatile WritePacketDirectToDB instance;

    private WritePacketDirectToDB() {

        super();
        ueLoginDao = (UELoginDao) SpringContainer.getSpringContainer().getBean("ueLoginDao");
        ueReportDao = (UEReportDao) SpringContainer.getSpringContainer().getBean("ueReportDao");
    }

    public static WritePacketDirectToDB getWritePacketDirectToDB() {

        if (instance == null) {

            synchronized (WritePacketDirectToDB.class) {

                if (instance == null) {

                    instance = new WritePacketDirectToDB();
                }
            }
        }

        return instance;
    }

    @Override
    protected void doWrite(UEPacket uePacket) {

        if (uePacket == null) {

            return;
        }

        Pojo pojo = uePacket.getPojo();

        if (pojo instanceof UELogin) {

            UELogin ueLogin = (UELogin) pojo;
            if (ueLogin == null || StringUtils.isEmpty(ueLogin.getUeSn())) {
                return;
            }
            ueLoginDao.addUELogin(ueLogin);
        } else if (pojo instanceof UEReport) {

            UEReport ueReport = (UEReport) pojo;
            ueReportDao.addUEReport(ueReport);
        }
    }
}
