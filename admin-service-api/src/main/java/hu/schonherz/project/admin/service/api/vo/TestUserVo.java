package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class TestUserVo extends TestBaseVo implements Serializable {

    private static final long serialVersionUID = 5534955718398254750L;

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String email;

}
