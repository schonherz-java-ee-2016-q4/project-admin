package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationForm {

    private String email;
    private String username;
    private String password;
    private String confirmPassword;

    public UserVo getUserVo() {
        UserVo vo = new UserVo();
        vo.setEmail(email);
        vo.setUsername(username);
        vo.setPassword(password);

        return vo;
    }

}
