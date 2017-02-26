package hu.schonherz.project.admin.web.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.web.view.security.SecurityManagerBean;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "companiesView")
@ViewScoped
@Data
@Slf4j
public class CompaniesView {

    private static final String CHANGING_SUCCESS = "growl_done";
    private static final String ACTIVATE_SUCCESS = "growl_activate_success_company";
    private static final String INACTIVATE_SUCCESS = "growl_inactivate_success_company";
    private List<CompanyVo> companies;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{securityManagerBean}")
    private SecurityManagerBean securityManagerBean;

    @EJB
    private CompanyServiceRemote companyServiceRemote;

    @PostConstruct
    public void init() {
        initializeList();
        companies = companyServiceRemote.findAll();
    }

    private void initializeList() {
        if (companies == null) {
            companies = new ArrayList<>();
        }
    }

    public void changeCompanyStatus(@NonNull final CompanyVo companyVo) {
        companyServiceRemote.changeStatus(companyVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null,
                new FacesMessage(localeManagerBean.localize(CHANGING_SUCCESS),
                        (companyVo.isActive() ? localeManagerBean.localize(INACTIVATE_SUCCESS)
                                : localeManagerBean.localize(ACTIVATE_SUCCESS)) + companyVo.getCompanyName()));
    }

    public void generateReport(@NonNull final CompanyVo companyVo) {
    }

}
