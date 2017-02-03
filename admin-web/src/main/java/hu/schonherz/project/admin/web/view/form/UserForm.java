package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserForm {

    private String email;
    private String username;
    private String password;

    public UserVo getUserVo() {
        UserVo vo = new UserVo();
        vo.setEmail(email);
        vo.setUsername(username);
        vo.setPassword(password);

        return vo;
    }

}
