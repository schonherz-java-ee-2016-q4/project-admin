package hu.schonherz.project.admin.data.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hu.schonherz.project.admin.data.quota.QuotasEntity;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
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

    @OneToOne
    private UserEntity adminUser;

    @Embedded
    private QuotasEntity quotas;

    @OneToMany
    @Column(nullable = false)
    private Set<UserEntity> agents;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

}
