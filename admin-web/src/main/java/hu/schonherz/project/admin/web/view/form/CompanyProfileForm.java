package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;


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
