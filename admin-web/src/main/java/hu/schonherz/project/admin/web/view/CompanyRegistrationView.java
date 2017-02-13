package hu.schonherz.project.admin.web.view;

import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyRegistrationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ManagedBean(name = "companyRegistrationView")
@ViewScoped
@Data
@Slf4j
public class CompanyRegistrationView {

    private CompanyRegistrationForm companyRegistrationForm;

    @EJB
    private UserServiceRemote userServiceRemote;

    @EJB
    private CompanyServiceRemote companyServiceRemote;

    @PostConstruct
    public void init() {
        companyRegistrationForm = new CompanyRegistrationForm();
    }

    public List<String> listEmails(String query) {
        List<String> emails = new ArrayList<>();
        for (UserVo userVo : userServiceRemote.findAll()) {
            if (userVo.getEmail().contains(query)) {
                emails.add(userVo.getEmail());
            }
        }
        return emails;
    }

    public void registration() {
//        FacesContext context = FacesContext.getCurrentInstance();
        CompanyVo companyVo = companyRegistrationForm.getCompanyVo();
        setDefaultValues(companyVo);
        try {
            companyServiceRemote.save(companyVo);
            log.info("Company '{}' successfully registered.", companyVo.getCompanyName());
        } catch (InvalidCompanyDataException e) {
            log.warn("Unsuccessful registration attempt with data:{}{} ", System.getProperty("line.separator"), companyRegistrationForm);
            log.warn("Causing exception:" + System.getProperty("line.separator"), e);
        }
    }

    private void setDefaultValues(final CompanyVo companyVo) {
        companyVo.setAgents(new HashSet<>());
        companyVo.setQuotes(new QuotasVo());
        companyVo.setActive(true);
    }

}
