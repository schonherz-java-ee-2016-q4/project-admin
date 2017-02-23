package hu.schonherz.project.remote.admin.api.rpc.issuetracker;

import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteQuotasVo;

public interface RemoteQuotasService {

    RemoteQuotasVo getQuotasOfCompany(String companyName);

}
