package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@ToString
@Slf4j
public class CompanyRegistrationForm {

    private String adminEmail;
    //    Vo fields
    private Long id;
    private String companyName;
    private String domainAddress;
    private UserVo adminUser;
    private QuotasVo quotes;
    private Set<UserVo> agents;
    private boolean active;

    public CompanyVo getCompanyVo() {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setId(id);
        companyVo.setCompanyName(companyName);
        companyVo.setDomainAddress(domainAddress);
        companyVo.setAdminUser(adminUser);
        companyVo.setQuotes(quotes);
        companyVo.setAgents(agents);
        companyVo.setActive(active);
        return companyVo;
    }

}
