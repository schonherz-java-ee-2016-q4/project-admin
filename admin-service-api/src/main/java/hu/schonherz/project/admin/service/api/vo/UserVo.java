package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Data
public class UserVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 4339751308790693753L;

    private String email;

    private String username;

    private String password;

    private boolean active;

    private boolean available;

    private boolean loggedIn;

    private UserRole userRole;

    private String companyName;

}
