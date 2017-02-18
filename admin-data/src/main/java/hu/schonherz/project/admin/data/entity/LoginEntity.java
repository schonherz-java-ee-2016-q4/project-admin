package hu.schonherz.project.admin.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "login", schema = "public")
public class LoginEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6201828861327999500L;
    private Long userId;
    private LocalDateTime loginDate;

}
