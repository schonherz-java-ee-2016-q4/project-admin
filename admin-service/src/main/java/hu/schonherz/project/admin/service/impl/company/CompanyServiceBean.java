package hu.schonherz.project.admin.service.impl.company;

import hu.schonherz.project.admin.data.entity.CompanyEntity;
import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.data.repository.CompanyRepository;
import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.mapper.company.CompanyEntityVoMapper;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import hu.schonherz.project.admin.service.mapper.user.UserEntityVoMapper;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public CompanyVo save(@NonNull final CompanyVo companyVo) {
        CompanyEntity companyEntity = CompanyEntityVoMapper.toEntity(companyVo);
        UserEntity userEntity = UserEntityVoMapper.toEntity(companyVo.getAdminUser());
        if (userEntity == null) {
            log.warn("Failed to get user from the '{}' company!", companyVo.getCompanyName());
        } else {
            userRepository.save(userEntity);
        }
        companyEntity = companyRepository.save(companyEntity);
        if (companyEntity == null) {
            log.warn("Failed to save company: " + companyVo.getCompanyName());
        }

        return CompanyEntityVoMapper.toVo(companyEntity);
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
    public void changeStatus(final Long id) {
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
