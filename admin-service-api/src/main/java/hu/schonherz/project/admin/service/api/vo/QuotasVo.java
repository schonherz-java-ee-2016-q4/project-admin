package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotasVo implements Serializable {

    private static final long serialVersionUID = 3457533455L;

    private int maxUsers;
    private int maxLoggedIn;
    private int maxDayTickets;
    private int maxWeekTicktes;
    private int maxMonthTickets;

}
