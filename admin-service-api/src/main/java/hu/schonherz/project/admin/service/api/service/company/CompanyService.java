package hu.schonherz.project.admin.service.api.service.company;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import java.util.List;

public interface CompanyService {

    CompanyVo findById(Long id);

    CompanyVo findByName(String companyName);

    List<CompanyVo> findAll();

    void changeStatus(Long id);

}
