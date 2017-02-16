package hu.schonherz.project.admin.service.api.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class LoginVo extends BaseVo {

    private Long userId;
    private LocalDateTime loginDate;
}
