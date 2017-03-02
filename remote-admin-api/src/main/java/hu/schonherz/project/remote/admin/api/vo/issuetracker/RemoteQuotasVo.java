package hu.schonherz.project.remote.admin.api.vo.issuetracker;

import java.io.Serializable;

public class RemoteQuotasVo implements Serializable {

    private static final long serialVersionUID = 3457533455L;

    private int maxDayTickets;
    private int maxWeekTickets;
    private int maxMonthTickets;

    public RemoteQuotasVo() {
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

    @Override
    public String toString() {
        return "RemoteQuotasVo{" + "maxDayTickets=" + maxDayTickets + ", maxWeekTickets=" + maxWeekTickets + ", maxMonthTickets=" + maxMonthTickets + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        final int base = 37;
        hash = base * hash + this.maxDayTickets;
        hash = base * hash + this.maxWeekTickets;
        hash = base * hash + this.maxMonthTickets;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RemoteQuotasVo other = (RemoteQuotasVo) obj;
        if (this.maxDayTickets != other.maxDayTickets) {
            return false;
        }
        if (this.maxWeekTickets != other.maxWeekTickets) {
            return false;
        }

        return this.maxMonthTickets == other.maxMonthTickets;
    }

}
