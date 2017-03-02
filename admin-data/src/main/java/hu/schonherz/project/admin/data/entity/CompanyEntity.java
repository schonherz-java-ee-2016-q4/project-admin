package hu.schonherz.project.admin.data.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "company", schema = "public")
public class CompanyEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String domainAddress;

    private String adminEmail;

    @OneToMany
    @Column(nullable = false)
    private Set<UserEntity> agents;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

//    Quotas
    @Column(nullable = false)
    private Integer maxUsers;
    @Column(nullable = false)
    private Integer maxLoggedIn;
    @Column(nullable = false)
    private Integer maxDayTickets;
    @Column(nullable = false)
    private Integer maxWeekTickets;
    @Column(nullable = false)
    private Integer maxMonthTickets;

}
