package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserData implements Serializable {

    private static final long serialVersionUID = 10L;

    private Long id;

    private String email;

    private String username;

    private String password;

    private UserRole userRole;

}
