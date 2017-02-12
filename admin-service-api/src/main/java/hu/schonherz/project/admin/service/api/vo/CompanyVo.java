package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -100L;

    @Data
    @Builder
    public static class Quotas {

        private int maxUsers;
        private int maxLoggedIn;
        private int maxDayTickets;
        private int maxWeekTicktes;
        private int maxMonthTickets;

    }

    private String name;
    private UserVo companyAdmin;
    private List<UserVo> agents;
    private Quotas quotes;
    private boolean active;

}
