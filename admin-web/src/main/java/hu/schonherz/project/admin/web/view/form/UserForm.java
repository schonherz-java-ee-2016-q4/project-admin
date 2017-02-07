package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserForm {

    private Long id;
    private String email;
    private String username;
    private String password;
    private UserRole userRole;

    public UserVo getUserVo() {
        UserVo vo = new UserVo();
        vo.setId(id);
        vo.setEmail(email);
        vo.setUsername(username);
        vo.setPassword(password);
        vo.setUserRole(userRole);

        return vo;
    }

}
