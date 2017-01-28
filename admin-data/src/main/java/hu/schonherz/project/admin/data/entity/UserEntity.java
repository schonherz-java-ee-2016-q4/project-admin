package hu.schonherz.project.admin.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "User", schema = "public")
public class UserEntity extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false)
    @Getter @Setter private String email;
    
//  This can be null, if the Administrator register a company,
//      because the email field is the only one, which is required!
    @Getter @Setter private String username;
    
    @Column(nullable = false)
    @Getter @Setter private String password;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Getter @Setter private boolean active;
}
