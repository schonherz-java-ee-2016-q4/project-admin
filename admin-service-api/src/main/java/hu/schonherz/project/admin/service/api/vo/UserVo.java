package hu.schonherz.project.admin.service.api.vo;

import hu.schonherz.project.admin.service.api.service.UserService;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 4339751308790693753L;

    public UserVo() {
        active = true;
        userRole = UserService.ROLE_AGENT;
        email = null;
        username = null;
        password = null;
    }

    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private boolean active;
    @Getter
    @Setter
    private String userRole;

}
