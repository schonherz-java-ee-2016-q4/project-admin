package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@ToString
@Slf4j
public class CompanyForm {

    private String adminEmail;
    // Vo fields
    private Long id;
    private String companyName;
    private String domainAddress;
    private QuotasVo quotes;
    private Set<UserVo> agents;
    private boolean active;

    public CompanyForm(@NonNull final CompanyVo companyVo) {
        setId(companyVo.getId());
        setCompanyName(companyVo.getCompanyName());
        setAdminEmail(companyVo.getAdminEmail());
        setQuotes(companyVo.getQuotas());
        setAgents(companyVo.getAgents());
        setActive(companyVo.isActive());
        setDomainAddress(companyVo.getDomainAddress());
    }

    public CompanyVo getCompanyVo() {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setId(id);
        companyVo.setCompanyName(companyName);
        companyVo.setDomainAddress(domainAddress);
        companyVo.setAdminEmail(adminEmail);
        companyVo.setQuotas(quotes);
        companyVo.setAgents(agents);
        companyVo.setActive(active);
        return companyVo;
    }

}
