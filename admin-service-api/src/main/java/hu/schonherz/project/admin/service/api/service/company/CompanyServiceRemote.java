package hu.schonherz.project.admin.service.api.service.company;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;

public interface CompanyServiceRemote extends CompanyService {

    CompanyVo save(CompanyVo companyVo) throws InvalidCompanyDataException;

}
