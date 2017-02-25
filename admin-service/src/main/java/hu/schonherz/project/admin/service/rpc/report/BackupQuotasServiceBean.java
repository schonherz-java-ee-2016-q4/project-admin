package hu.schonherz.project.admin.service.rpc.report;

import hu.schonherz.javatraining.issuetracker.shared.api.ForAdminServiceRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless(mappedName = "BackupReportServiceBean")
@Local(ForAdminServiceRemote.class)
public class BackupQuotasServiceBean implements ForAdminServiceRemote {

    @Override
    public TicketCreationReportData getTicketCreationByCompanyReport(String string) {
        TicketCreationReportData reportData = new TicketCreationReportData();
        final int today = 2;
        final int thisWeek = 10;
        final int thisMonth = 35;
        reportData.setToday(today);
        reportData.setOnThisWeek(thisWeek);
        reportData.setOnThisMonth(thisMonth);

        return reportData;
    }

}
