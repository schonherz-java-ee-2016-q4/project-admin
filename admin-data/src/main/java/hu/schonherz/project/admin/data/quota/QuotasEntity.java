package hu.schonherz.project.admin.data.quota;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class QuotasEntity implements Serializable {

    @Column(nullable = false)
    private int maxUsers;
    @Column(nullable = false)
    private int maxLoggedIn;
    @Column(nullable = false)
    private int maxDayTickets;
    @Column(nullable = false)
    private int maxWeekTicktes;
    @Column(nullable = false)
    private int maxMonthTickets;

}
