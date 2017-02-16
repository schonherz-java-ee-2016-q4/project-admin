package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileForm extends UserForm {

    private String newPassword;

    private String confirmNewPassword;

    public ProfileForm(@NonNull final UserVo userVo) {
        super(userVo);
    }

}
