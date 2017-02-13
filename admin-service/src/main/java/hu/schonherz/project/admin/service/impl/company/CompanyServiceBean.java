package hu.schonherz.project.admin.service.impl.company;

import hu.schonherz.project.admin.data.entity.CompanyEntity;
import hu.schonherz.project.admin.data.repository.CompanyRepository;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.mapper.company.CompanyEntityVoMapper;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

@Stateless(mappedName = "CompanyServiceBean")
@Local(CompanyServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Slf4j
public class CompanyServiceBean implements CompanyServiceLocal {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public CompanyVo save(@NonNull final CompanyVo companyVo) {
        CompanyEntity entity = CompanyEntityVoMapper.toEntity(companyVo);
        entity = companyRepository.save(entity);
        if (entity == null) {
            log.warn("Failed to save company: " + companyVo.getCompanyName());
        }

        return CompanyEntityVoMapper.toVo(entity);
    }

    @Override
    public CompanyVo findById(@NonNull final Long id) {
        CompanyEntity entity = companyRepository.findOne(id);
        if (entity == null) {
            log.warn("Company with id " + id + " doesn't exist");
        }

        return CompanyEntityVoMapper.toVo(entity);
    }

    @Override
    public CompanyVo findByName(@NonNull final String companyName) {
        CompanyEntity entity = companyRepository.findByCompanyName(companyName);
        if (entity == null) {
            log.warn("No company found with name " + companyName);
        }

        return CompanyEntityVoMapper.toVo(entity);
    }

    @Override
    public List<CompanyVo> findAll() {
        return CompanyEntityVoMapper.toVo(companyRepository.findAll());
    }

    @Override
    public void changeStatus(Long id) {
        CompanyEntity entity = companyRepository.findOne(id);
        if (entity == null) {
            log.warn("Company with id " + id + " does not exist. Cannot change status");
            return;
        }

        entity.setActive(!entity.isActive());
        if (companyRepository.save(entity) == null) {
            log.warn("Failed to change status of company: " + entity.getCompanyName());
        }
    }

}
