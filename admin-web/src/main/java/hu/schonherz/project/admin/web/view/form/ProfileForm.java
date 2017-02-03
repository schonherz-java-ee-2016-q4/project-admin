package hu.schonherz.project.admin.web.view.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ProfileForm extends UserForm {

    @Getter
    @Setter
    private String newPassword;

    @Getter
    @Setter
    private String confirmNewPassword;

}
