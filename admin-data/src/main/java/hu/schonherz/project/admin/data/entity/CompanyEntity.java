package hu.schonherz.project.admin.data.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hu.schonherz.project.admin.data.quota.Quotas;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company", schema = "public")
public class CompanyEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String companyName;

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private UserEntity adminUser;

    @Column(nullable = false)
    @Getter
    @Setter
    private Quotas quotas;

    @OneToMany
    @Column(nullable = false)
    @Getter
    @Setter
    private Set<UserEntity> agents;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Getter
    @Setter
    private boolean active;

}
