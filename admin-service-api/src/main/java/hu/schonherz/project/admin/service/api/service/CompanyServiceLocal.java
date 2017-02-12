package hu.schonherz.project.admin.service.api.service;

import hu.schonherz.project.admin.service.api.vo.CompanyVo;

public interface CompanyServiceLocal extends CompanyService {

    CompanyVo save(CompanyVo companyVo);

}
