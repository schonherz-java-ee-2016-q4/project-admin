package hu.schonherz.project.admin.web.view.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class RegistrationForm extends UserForm {

    @Getter
    @Setter
    private String confirmPassword;

}
