package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserForm {

    protected Long id;
    protected String email;
    protected String username;
    protected String password;

    public UserVo getUserVo() {
        UserVo vo = new UserVo();
        vo.setId(id);
        vo.setEmail(email);
        vo.setUsername(username);
        vo.setPassword(password);

        return vo;
    }

}
