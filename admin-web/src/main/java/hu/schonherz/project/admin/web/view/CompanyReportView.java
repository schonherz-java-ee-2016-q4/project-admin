package hu.schonherz.project.admin.web.view;

import hu.schonherz.project.admin.service.api.report.ReportServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.ReportVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import hu.schonherz.project.admin.web.view.security.SecurityManagerBean;
import java.io.Serializable;
import java.util.Random;
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

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @ManagedProperty(value = "#{securityManagerBean}")
    private SecurityManagerBean securityManager;

    @EJB
    private CompanyServiceRemote companyServiceRemote;
    private CompanyForm companyProfileForm;

    @EJB
    private ReportServiceRemote reportServiceRemote;

    private ReportVo reportVo;

    private LineChartModel animatedModel;

    private LineChartModel animatedModelMonthly;

    private LineChartModel animatedModelAnnual;

    private int dailyUsage;

    private int weeklyUsage;

    private int monthlyUsage;

    public LineChartModel getAnimatedDaily() {
        return animatedModel;
    }

    public LineChartModel getAnimatedModelMonthly() {
        return animatedModelMonthly;
    }

    public LineChartModel getAnimatedModelAnnual() {
        return animatedModelAnnual;
    }

    @PostConstruct
    public void init() {
        if (!securityManager.isPagePermitted(NavigatorBean.Pages.COMPANY_PROFILE, false)) {
            return;
        }

        FacesContext context = FacesContext.getCurrentInstance();
        UserVo userVo = getLoggedInUser(context);
        String companyIdParameter = context.getExternalContext().getRequestParameterMap().get("id");
        if (companyIdParameter == null) {
            if (!securityManager.isUserCompanyAdmin()) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_LIST);
                return;
            } else {
                Long companyId = companyServiceRemote.findByName(userVo.getCompanyName()).getId();
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_REPORT, "id", companyId);
                return;
            }
        }
        CompanyVo currentCompanyVo = companyServiceRemote.findById(Long.valueOf(companyIdParameter));
        companyProfileForm = new CompanyForm(currentCompanyVo);

        reportVo = reportServiceRemote.generateReportFor(currentCompanyVo.getCompanyName());

        final int hundred = 100;
        dailyUsage = (int) Math.round((double) reportVo.getUsedTodayTickets() / reportVo.getMaxTodayTickets() * hundred);
        weeklyUsage = (int) Math.round((double) reportVo.getUsedThisWeekTickets() / reportVo.getMaxThisWeekTickets() * hundred);
        monthlyUsage = (int) Math.round((double) reportVo.getUsedThisMonthsTickets() / reportVo.getMaxThisMonthsTickets() * hundred);

        createAnimatedModels();
    }

    private UserVo getLoggedInUser(final FacesContext context) {
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (UserVo) session.getAttribute("user");
    }

    private void createAnimatedModels() {
        animatedModel = initLinearModel();
        animatedModel.setAnimate(true);
        animatedModel.setLegendPosition("se");
        Axis yAxisLoginsEachDay = animatedModel.getAxis(AxisType.Y);
        final int min = 0;
        final int max = reportVo.getLoginsThisWeek();
        yAxisLoginsEachDay.setMin(min);
        yAxisLoginsEachDay.setMax(max);

    }

    private LineChartModel initLinearModel() {
        final int arrayLength = 7;
        int[] dailyLogins = new int[arrayLength];
        final int firstIndex = 0;
        dailyLogins[firstIndex] = reportVo.getLoginsToday();
        int allWeekLogins = reportVo.getLoginsThisWeek() - reportVo.getLoginsToday();

        Random rnd = new Random();
        final int start = 1;
        for (int i = start; i < dailyLogins.length; i++) {
            // If there are no logins left, every other day will have 0 logins
            if (allWeekLogins <= 0) {
                dailyLogins[i] = 0;
                continue;
            }
            // If this is the last day, give it every remaining logins
            if (i == dailyLogins.length - 1) {
                dailyLogins[i] = allWeekLogins;
                allWeekLogins = 0;
                break;
            }

            dailyLogins[i] = rnd.nextInt(allWeekLogins);
            allWeekLogins -= dailyLogins[i];
        }

        LineChartModel model = new LineChartModel();

        LineChartSeries days = new LineChartSeries();
        days.setLabel("Daily logins");

        for (int i = dailyLogins.length - 1; i >= 0; i--) {
            days.set(dailyLogins.length - i, dailyLogins[i]);
        }

        model.addSeries(days);

        return model;
    }

    public NavigatorBean getNavigator() {
        return navigator;
    }

    public void setNavigator(final NavigatorBean navigator) {
        this.navigator = navigator;
    }

    public SecurityManagerBean getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(final SecurityManagerBean securityManager) {
        this.securityManager = securityManager;
    }

    public CompanyServiceRemote getCompanyServiceRemote() {
        return companyServiceRemote;
    }

    public void setCompanyServiceRemote(final CompanyServiceRemote companyServiceRemote) {
        this.companyServiceRemote = companyServiceRemote;
    }

    public CompanyForm getCompanyProfileForm() {
        return companyProfileForm;
    }

    public void setCompanyProfileForm(final CompanyForm companyProfileForm) {
        this.companyProfileForm = companyProfileForm;
    }

    public ReportServiceRemote getReportServiceRemote() {
        return reportServiceRemote;
    }

    public void setReportServiceRemote(final ReportServiceRemote reportServiceRemote) {
        this.reportServiceRemote = reportServiceRemote;
    }

    public ReportVo getReportVo() {
        return reportVo;
    }

    public void setReportVo(final ReportVo reportVo) {
        this.reportVo = reportVo;
    }

    public LineChartModel getAnimatedModel() {
        return animatedModel;
    }

    public void setAnimatedModel(final LineChartModel animatedModel) {
        this.animatedModel = animatedModel;
    }

    public int getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(final int dailyUsage) {
        this.dailyUsage = dailyUsage;
    }

    public int getWeeklyUsage() {
        return weeklyUsage;
    }

    public void setWeeklyUsage(final int weeklyUsage) {
        this.weeklyUsage = weeklyUsage;
    }

    public int getMonthlyUsage() {
        return monthlyUsage;
    }

    public void setMonthlyUsage(final int monthlyUsage) {
        this.monthlyUsage = monthlyUsage;
    }

}
