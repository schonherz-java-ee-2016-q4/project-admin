package hu.schonherz.project.admin.service.api.report;

import hu.schonherz.project.admin.service.api.vo.ReportVo;

public interface ReportServiceRemote {

    ReportVo generateReportFor(String companyName);

}
