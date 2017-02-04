package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
public class ProfileForm extends UserForm {

    @Getter
    @Setter
    private String newPassword;

    @Getter
    @Setter
    private String confirmNewPassword;

    public ProfileForm(UserVo userVo) {
        id = userVo.getId();
        email = userVo.getEmail();
        username = userVo.getUsername();
    }

}
