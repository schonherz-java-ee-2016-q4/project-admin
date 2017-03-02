package hu.schonherz.project.admin.service.rpc.report;

import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;
import hu.schonherz.project.admin.service.api.report.BackupQuotasService;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import lombok.NonNull;

@Stateless(mappedName = "BackupReportServiceBean")
@Local(BackupQuotasService.class)
public class BackupQuotasServiceBean implements BackupQuotasService {

    private static final Map<Long, TicketCreationReportData> SAVED_QUOTA_REPORTS;

    @EJB
    private CompanyServiceLocal companyService;

    static {
        SAVED_QUOTA_REPORTS = new HashMap<>();
    }

    @Override
    public TicketCreationReportData getTicketCreationByCompanyReport(@NonNull final String companyName) {
        CompanyVo companyVo = companyService.findByName(companyName);
        if (companyVo == null) {
            return null;
        }
        TicketCreationReportData reportData = SAVED_QUOTA_REPORTS.get(companyVo.getId());
        QuotasVo companyQuotas = companyVo.getQuotas();
        // If there is no real data, generate random values and save it
        if (reportData == null) {
            reportData = new TicketCreationReportData();
            Random rnd = new Random();
            int today = rnd.nextInt(companyQuotas.getMaxDayTickets());
            int thisWeek = today + rnd.nextInt(companyQuotas.getMaxWeekTickets() - today);
            int thisMonth = thisWeek + rnd.nextInt(companyQuotas.getMaxMonthTickets() - thisWeek);

            reportData.setToday(today);
            reportData.setOnThisWeek(thisWeek);
            reportData.setOnThisMonth(thisMonth);

            SAVED_QUOTA_REPORTS.put(companyVo.getId(), reportData);
//            log.info("saved Quota Reports: {}", savedQuotaReports.toString());
            return reportData;
        }

        // If usage is very close to the limit, don't add more tickets
        final int one = 1;
        if (reportData.getToday() >= companyQuotas.getMaxDayTickets() - one
                || reportData.getOnThisWeek() >= companyQuotas.getMaxWeekTickets() - one
                || reportData.getOnThisMonth() >= companyQuotas.getMaxMonthTickets() - one) {
            return reportData;
        }

        // Randomly add one more ticket
        final double treshold = 0.6;
        if (Math.random() > treshold) {
            reportData.setToday(reportData.getToday() + one);
            reportData.setOnThisWeek(reportData.getOnThisWeek() + one);
            reportData.setOnThisMonth(reportData.getOnThisMonth() + one);
        }

        return reportData;
    }

    @Override
    public void saveRealUsage(@NonNull final Long companyId, @NonNull final TicketCreationReportData quotaUsage) {
        SAVED_QUOTA_REPORTS.put(companyId, quotaUsage);
    }

}
