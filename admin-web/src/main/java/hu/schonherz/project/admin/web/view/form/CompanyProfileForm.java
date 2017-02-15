package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@NoArgsConstructor
@ToString
@Data
public class CompanyProfileForm extends CompanyRegistrationForm{

    public CompanyProfileForm(@NonNull final CompanyVo companyVo) {
        setId(companyVo.getId());
        setCompanyName(companyVo.getCompanyName());
        setAdminUser(companyVo.getAdminUser());
        setAdminEmail(companyVo.getAdminUser().getEmail());
        setQuotes(companyVo.getQuotes());
        setAgents(companyVo.getAgents());
        setActive(companyVo.isActive());
    }

}
