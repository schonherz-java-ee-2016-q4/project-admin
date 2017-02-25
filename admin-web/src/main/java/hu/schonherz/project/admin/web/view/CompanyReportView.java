package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name = "companyReportView")
@ViewScoped
@Slf4j
public class CompanyReportView implements Serializable {

    private static final long serialVersionUID = 4342L;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @EJB
    private UserServiceRemote userServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;

    private CompanyForm companyProfileForm;

    private LineChartModel animatedModelDaily;

    private LineChartModel animatedModelMonthly;

    private LineChartModel animatedModelAnnual;

    private int dailyUsage;

    private int weeklyUsage;

    private int monthlyUsage;

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
        CompanyVo currentCompanyVo = companyServiceRemote.findById(Long.valueOf(companyIdParameter));
        companyProfileForm = new CompanyForm(currentCompanyVo);

        final int mockedData = 35;
        dailyUsage = mockedData;
        weeklyUsage = mockedData;
        monthlyUsage = mockedData;

        createAnimatedModels();
    }

    private UserVo getLoggedInUser(final FacesContext context) {
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (UserVo) session.getAttribute("user");
    }

    private void createAnimatedModels() {
        animatedModelDaily = initLinearModelDaily();
        animatedModelDaily.setAnimate(true);
        animatedModelDaily.setLegendPosition("se");
        Axis yAxisLoginsEachDay = animatedModelDaily.getAxis(AxisType.Y);
        final int min = 0;
        final int max = 10;
        yAxisLoginsEachDay.setMin(min);
        yAxisLoginsEachDay.setMax(max);

        animatedModelMonthly = initLinearModelMonthly();
        animatedModelMonthly.setAnimate(true);
        animatedModelMonthly.setLegendPosition("se");
        Axis yAxisLoginsEachMonth = animatedModelMonthly.getAxis(AxisType.Y);
        yAxisLoginsEachMonth.setMin(min);
        yAxisLoginsEachMonth.setMax(max);

        animatedModelAnnual = initLinearModelAnnual();
        animatedModelAnnual.setAnimate(true);
        animatedModelAnnual.setLegendPosition("se");
        Axis yAxisLoginsEachYear = animatedModelAnnual.getAxis(AxisType.Y);
        yAxisLoginsEachYear.setMin(min);
        yAxisLoginsEachYear.setMax(max);
    }

    private LineChartModel initLinearModelDaily() {
        LineChartModel model = new LineChartModel();

        LineChartSeries days = new LineChartSeries();
        days.setLabel("Daily logins");

        final int start = 1;
        final int end = 6;
        for (int i = start; i < end; i++) {
            days.set(i, i);
        }

        model.addSeries(days);

        return model;
    }

    private LineChartModel initLinearModelMonthly() {
        LineChartModel model = new LineChartModel();

        LineChartSeries months = new LineChartSeries();
        months.setLabel("Monthly logins");

        final int start = 1;
        final int end = 6;
        for (int i = start; i < end; i++) {
            months.set(i, i);
        }

        model.addSeries(months);

        return model;
    }

    private LineChartModel initLinearModelAnnual() {
        LineChartModel model = new LineChartModel();

        LineChartSeries years = new LineChartSeries();
        years.setLabel("Annual logins");

        final int start = 2012;
        final int end = 2017;
        for (int i = start; i < end; i++) {
            years.set(i, i - start);
        }

        model.addSeries(years);

        return model;
    }

    public CompanyForm getCompanyProfileForm() {
        return companyProfileForm;
    }

    public void setCompanyProfileForm(CompanyForm companyProfileForm) {
        this.companyProfileForm = companyProfileForm;
    }

    public int getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(int dailyUsage) {
        this.dailyUsage = dailyUsage;
    }

    public int getWeeklyUsage() {
        return weeklyUsage;
    }

    public void setWeeklyUsage(int weeklyUsage) {
        this.weeklyUsage = weeklyUsage;
    }

    public int getMonthlyUsage() {
        return monthlyUsage;
    }

    public void setMonthlyUsage(int monthlyUsage) {
        this.monthlyUsage = monthlyUsage;
    }

    public LocaleManagerBean getLocaleManagerBean() {
        return localeManagerBean;
    }

    public void setLocaleManagerBean(LocaleManagerBean localeManagerBean) {
        this.localeManagerBean = localeManagerBean;
    }

    public NavigatorBean getNavigator() {
        return navigator;
    }

    public void setNavigator(NavigatorBean navigator) {
        this.navigator = navigator;
    }

    public UserServiceRemote getUserServiceRemote() {
        return userServiceRemote;
    }

    public void setUserServiceRemote(UserServiceRemote userServiceRemote) {
        this.userServiceRemote = userServiceRemote;
    }

    public CompanyServiceRemote getCompanyServiceRemote() {
        return companyServiceRemote;
    }

    public void setCompanyServiceRemote(CompanyServiceRemote companyServiceRemote) {
        this.companyServiceRemote = companyServiceRemote;
    }

    public LineChartModel getAnimatedModelDaily() {
        return animatedModelDaily;
    }

    public void setAnimatedModelDaily(LineChartModel animatedModelDaily) {
        this.animatedModelDaily = animatedModelDaily;
    }

}
