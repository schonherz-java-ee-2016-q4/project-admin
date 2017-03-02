package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;

public class QuotasVo implements Serializable {

    private static final long serialVersionUID = 3457533455L;

    private int maxUsers;
    private int maxLoggedIn;
    private int maxDayTickets;
    private int maxWeekTickets;
    private int maxMonthTickets;

    public QuotasVo() {
        final int quotaLimit = 3;
        maxUsers = quotaLimit;
        maxLoggedIn = quotaLimit;
        maxDayTickets = quotaLimit;
        maxWeekTickets = quotaLimit;
        maxMonthTickets = quotaLimit;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getMaxLoggedIn() {
        return maxLoggedIn;
    }

    public void setMaxLoggedIn(int maxLoggedIn) {
        this.maxLoggedIn = maxLoggedIn;
    }

    public int getMaxDayTickets() {
        return maxDayTickets;
    }

    public void setMaxDayTickets(int maxDayTickets) {
        this.maxDayTickets = maxDayTickets;
    }

    public int getMaxWeekTickets() {
        return maxWeekTickets;
    }

    public void setMaxWeekTickets(int maxWeekTickets) {
        this.maxWeekTickets = maxWeekTickets;
    }

    public int getMaxMonthTickets() {
        return maxMonthTickets;
    }

    public void setMaxMonthTickets(int maxMonthTickets) {
        this.maxMonthTickets = maxMonthTickets;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
