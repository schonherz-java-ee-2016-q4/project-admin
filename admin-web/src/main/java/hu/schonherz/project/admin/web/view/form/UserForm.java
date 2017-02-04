package hu.schonherz.project.admin.web.view.form;

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

    public UserVo getUserVo() {
        UserVo vo = new UserVo();
        vo.setId(id);
        vo.setEmail(email);
        vo.setUsername(username);
        vo.setPassword(password);

        return vo;
    }

}
