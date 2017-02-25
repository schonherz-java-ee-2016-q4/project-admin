package hu.schonherz.project.admin.service.rpc.report;

import hu.schonherz.javatraining.issuetracker.shared.api.ForAdminServiceRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;
import hu.schonherz.project.admin.service.api.login.LoginService;
import hu.schonherz.project.admin.service.api.report.ReportServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.ReportVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Stateless(mappedName = "ReportServiceBean")
@Remote(ReportServiceRemote.class)
@Data
@Slf4j
public class ReportServiceBean implements ReportServiceRemote {

    private static final String JNDI_REPORT_SERVICE = "java:global/issue-tracker-ear-0.0.1-SNAPSHOT/issue-tracker-service-0.0.1-SNAPSHOT"
            + "/ForAdminServiceBean!hu.schonherz.javatraining.issuetracker.shared.api.ForAdminServiceRemote";

    // Inject in constructor via context lookup, because @EJB(lookup....) makes build fail in case of not finding bean
    private ForAdminServiceRemote remoteReportService;
    @EJB
    private ForAdminServiceRemote backupReportService;
    @EJB
    private LoginService loginService;
    @EJB
    private CompanyServiceLocal companyService;

    public ReportServiceBean() {
        try {
            Context context = new InitialContext();
            remoteReportService = (ForAdminServiceRemote) context.lookup(JNDI_REPORT_SERVICE);
        } catch (NamingException ne) {
            log.warn("Failed to acquire remote report service. Going to use backup report service.", ne);
        }

    }

    @Override
    public ReportVo generateReportFor(@NonNull final String companyName) {
        if (remoteReportService == null) {
            remoteReportService = backupReportService;
        }

        // Trying to acquire data for quota report generation
        CompanyVo company = companyService.findByName(companyName);
        if (company == null) {
            log.error("There no company with name: '{}'. Canceling report generation!", companyName);
            return null;
        }
        TicketCreationReportData quotaUsageData = remoteReportService.getTicketCreationByCompanyReport(companyName);
        if (quotaUsageData == null) {
            log.warn("Failed to receive quota usage data from issuetracker. Using generated data.");
            quotaUsageData = backupReportService.getTicketCreationByCompanyReport(companyName);
        }

        // Start creating the reports
        ReportVo reportVo = new ReportVo();
        setQuotaReports(reportVo, company.getQuotas(), quotaUsageData);

        // Prepare recquired data for login report generation
        Set<Long> agentIds = company.getAgents().stream()
                .map(UserVo::getId)
                .collect(Collectors.toSet());
        List<LocalDate> allCompanyLogins = new ArrayList<>();
        for (Long id : agentIds) {
            loginService.findByUserId(id).stream()
                    .map(loginVo -> loginVo.getLoginDate().toLocalDate())
                    .forEach(allCompanyLogins::add);
        }

        setLoginReports(reportVo, allCompanyLogins);

        return reportVo;
    }

    private void setQuotaReports(ReportVo reportVo, QuotasVo companyQuotas, TicketCreationReportData quotaUsageData) {
        reportVo.setMaxTodayTickets(companyQuotas.getMaxDayTickets());
        reportVo.setUsedTodayTickets(quotaUsageData.getToday());

        reportVo.setMaxThisWeekTickets(companyQuotas.getMaxWeekTickets());
        reportVo.setUsedThisWeekTickets(quotaUsageData.getOnThisWeek());

        reportVo.setMaxThisMonthsTickets(companyQuotas.getMaxMonthTickets());
        reportVo.setUsedThisMonthsTickets(quotaUsageData.getOnThisMonth());
    }

    private void setLoginReports(ReportVo reportVo, List<LocalDate> allCompanyLogins) {
        // Generate today login report
        LocalDate now = LocalDate.now();
        int todayLogins = (int) allCompanyLogins.stream()
                .filter(now::equals)
                .count();

        // Generate this week login report
        final int eight = 8;
        LocalDate eightDaysBefore = now.minusDays(eight);
        int thisWeekLogins = (int) allCompanyLogins.stream()
                .filter(loginDate -> loginDate.isAfter(eightDaysBefore))
                .count();

        // Generate this month login report
        int thisYear = now.getYear();
        Month thisMonth = now.getMonth();
        int thisMonthLogins = (int) allCompanyLogins.stream()
                .filter(loginDate -> loginDate.getYear() == thisYear && loginDate.getMonth() == thisMonth)
                .count();

        reportVo.setLoginsToday(todayLogins);
        reportVo.setLoginsThisWeek(thisWeekLogins);
        reportVo.setLoginsThisMonths(thisMonthLogins);
    }

}
