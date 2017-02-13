package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@ToString
public class CompanyRegistrationForm {

    private String adminEmail;
    //    Vo fields
    private String companyName;
    private UserVo adminUser;
    private QuotasVo quotes;
    private Set<UserVo> agents;
    private boolean active;

    public CompanyVo getCompanyVo() {
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompanyName(companyName);
        companyVo.setAdminUser(adminUser);
        companyVo.setQuotes(quotes);
        companyVo.setAgents(agents);
        companyVo.setActive(active);
        return companyVo;
    }
}
