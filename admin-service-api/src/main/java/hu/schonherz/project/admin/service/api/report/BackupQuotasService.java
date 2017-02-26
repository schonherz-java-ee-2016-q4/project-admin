package hu.schonherz.project.admin.service.api.report;

import hu.schonherz.javatraining.issuetracker.shared.api.ForAdminServiceRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;

public interface BackupQuotasService extends ForAdminServiceRemote {

    void saveRealUsage(Long companyId, TicketCreationReportData quotaUsage);

}
