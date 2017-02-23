package hu.schonherz.project.admin.web.view;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "companyReportView")
@ViewScoped
@Data
@Slf4j
public class CompanyReportView implements Serializable {

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @EJB
    private UserServiceRemote userServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;

    private CompanyVo currentCompanyVo;

    private CompanyForm companyProfileForm;

    private LineChartModel animatedModelDaily;
    
    private LineChartModel animatedModelMonthly;
    
    private LineChartModel animatedModelAnnual;
    
    int dailyUsage;
    
    int weeklyUsage;
    
    int monthlyUsage;
    
    public LineChartModel getAnimatedDaily() {
        return animatedModelDaily;
    }
    
    public LineChartModel getAnimatedModelMonthly() {
        return animatedModelMonthly;
    }
    
    public LineChartModel getAnimatedModelAnnual() {
        return animatedModelAnnual;
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserVo userVo = getLoggedInUser(context);
        String companyIdParameter = context.getExternalContext().getRequestParameterMap().get("id");
        if (companyIdParameter == null) {
            if (userVo.getUserRole().equals(UserRole.ADMIN)) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_LIST);
            } else {
                Long companyId = companyServiceRemote.findByName(userVo.getCompanyName()).getId();
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyId);
            }
        } else if (userVo.getUserRole().equals(UserRole.COMPANY_ADMIN)) {
            Long companyId = companyServiceRemote.findByName(userVo.getCompanyName()).getId();
            if (!companyId.equals(Long.valueOf(companyIdParameter))) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyId);
            }
        }
        currentCompanyVo = companyServiceRemote.findById(Long.valueOf(companyIdParameter));
        companyProfileForm = new CompanyForm(currentCompanyVo);
        
        int mockedData = 35;
        dailyUsage = mockedData;
        weeklyUsage = mockedData;
        monthlyUsage = mockedData;
        
        createAnimatedModels();
    }

    private UserVo getLoggedInUser(FacesContext context) {
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (UserVo) session.getAttribute("user");
    }
    
    private void createAnimatedModels() {
        animatedModelDaily = initLinearModelDaily();
        animatedModelDaily.setAnimate(true);
        animatedModelDaily.setLegendPosition("se");
        Axis yAxisLoginsEachDay = animatedModelDaily.getAxis(AxisType.Y);
        yAxisLoginsEachDay.setMin(0);
        yAxisLoginsEachDay.setMax(10);
        
        animatedModelMonthly = initLinearModelDaily();
        animatedModelMonthly.setAnimate(true);
        animatedModelMonthly.setLegendPosition("se");
        Axis yAxisLoginsEachMonth = animatedModelMonthly.getAxis(AxisType.Y);
        yAxisLoginsEachMonth.setMin(0);
        yAxisLoginsEachMonth.setMax(10);
        
        animatedModelAnnual = initLinearModelDaily();
        animatedModelAnnual.setAnimate(true);
        animatedModelAnnual.setLegendPosition("se");
        Axis yAxisLoginsEachYear = animatedModelAnnual.getAxis(AxisType.Y);
        yAxisLoginsEachYear.setMin(0);
        yAxisLoginsEachYear.setMax(10);
    }
     
    private LineChartModel initLinearModelDaily() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
        
        model.addSeries(series1);
         
        return model;
    }
    
    private LineChartModel initLinearModelMonthly() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
        
        model.addSeries(series1);
         
        return model;
    }
    
    private LineChartModel initLinearModelAnnual() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
        
        model.addSeries(series1);
         
        return model;
    }
    
}