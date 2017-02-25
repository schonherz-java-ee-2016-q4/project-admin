package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportVo implements Serializable {

    private static final long serialVersionUID = 2112678L;

    // Quotas
    private int maxTodayTickets;
    private int usedTodayTickets;

    private int maxThisWeekTickets;
    private int usedThisWeekTickets;

    private int maxThisMonthsTickets;
    private int usedThisMonthsTickets;

    // Login
    private int loginsToday;
    private int loginsThisWeek;
    private int loginsThisMonths;

}
