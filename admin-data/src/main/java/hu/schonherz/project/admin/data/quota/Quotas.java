package hu.schonherz.project.admin.data.quota;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class Quotas {
    @Getter
    @Setter
    private int maxUsers;
    
    @Getter
    @Setter
    private int maxLoggedIn;
    
    @Getter
    @Setter
    private int maxDayTickets;
    
    @Getter
    @Setter
    private int maxWeekTicktes;
    
    @Getter
    @Setter
    private int maxMonthTickets;
}
