package hu.schonherz.project.admin.service.impl.rpc.issuetracker;

import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteQuotasService;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteQuotasVo;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;

@Stateless(mappedName = "RemoteQuotasServiceBean")
@Remote(RemoteQuotasService.class)
@Slf4j
public class RemoteQuotasServiceBean implements RemoteQuotasService {

    @EJB
    private CompanyServiceLocal companyService;

    @Override
    public RemoteQuotasVo getQuotasOfCompany(String companyName) {
        CompanyVo companyVo = companyService.findByName(companyName);
        if (companyVo == null) {
            log.warn("Tried to get quotas of non existing company: {}", companyName);
            return null;
        }

        QuotasVo quotasVo = companyVo.getQuotas();
        RemoteQuotasVo remoteVo = new RemoteQuotasVo();
        remoteVo.setMaxDayTickets(quotasVo.getMaxDayTickets());
        remoteVo.setMaxLoggedIn(quotasVo.getMaxLoggedIn());
        remoteVo.setMaxMonthTickets(quotasVo.getMaxMonthTickets());
        remoteVo.setMaxUsers(quotasVo.getMaxUsers());
        remoteVo.setMaxWeekTickets(quotasVo.getMaxWeekTickets());

        log.info("Returning quotas of company: {}", companyName);
        return remoteVo;
    }

}
