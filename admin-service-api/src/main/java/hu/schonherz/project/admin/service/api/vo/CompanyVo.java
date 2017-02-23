package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -100L;

    private String companyName;
    private String domainAddress;
    private String adminEmail;
    private QuotasVo quotes;
    private Set<UserVo> agents;
    private boolean active;

}
