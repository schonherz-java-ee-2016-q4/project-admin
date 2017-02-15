package hu.schonherz.project.admin.service.mapper.company;

import hu.schonherz.project.admin.data.entity.CompanyEntity;
import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.data.quota.QuotasEntity;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.user.UserEntityVoMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public final class CompanyEntityVoMapper {

    private static final Mapper MAPPER;

    static {
        MAPPER = new DozerBeanMapper();
    }

    private CompanyEntityVoMapper() {
    }

    public static CompanyVo toVo(final CompanyEntity entity) {
        if (entity == null) {
            return null;
        }
        CompanyVo companyVo = new CompanyVo();
        QuotasVo quotasVo = new QuotasVo();
        
        quotasVo.setMaxUsers(entity.getQuotas().getMaxUsers());
        quotasVo.setMaxLoggedIn(entity.getQuotas().getMaxLoggedIn());
        quotasVo.setMaxDayTickets(entity.getQuotas().getMaxDayTickets());
        quotasVo.setMaxWeekTickets(entity.getQuotas().getMaxWeekTickets());
        quotasVo.setMaxMonthTickets(entity.getQuotas().getMaxMonthTickets());
        
        companyVo.setId(entity.getId());
        companyVo.setCompanyName(entity.getCompanyName());
        companyVo.setAdminUser(UserEntityVoMapper.toVo(entity.getAdminUser()));
        companyVo.setAgents((Set<UserVo>) UserEntityVoMapper.toVo(entity.getAgents()));
        companyVo.setActive(entity.isActive());
        companyVo.setQuotes(quotasVo);
        
        return companyVo;
    }

    public static CompanyEntity toEntity(final CompanyVo vo) {
        if (vo == null) {
            return null;
        }
        CompanyEntity companyEntity = new CompanyEntity();
        QuotasEntity quotasEntity = new QuotasEntity();
        
        quotasEntity.setMaxUsers(vo.getQuotes().getMaxUsers());
        quotasEntity.setMaxLoggedIn(vo.getQuotes().getMaxLoggedIn());
        quotasEntity.setMaxDayTickets(vo.getQuotes().getMaxDayTickets());
        quotasEntity.setMaxWeekTickets(vo.getQuotes().getMaxWeekTickets());
        quotasEntity.setMaxMonthTickets(vo.getQuotes().getMaxMonthTickets());
        
        companyEntity.setId(vo.getId());
        companyEntity.setCompanyName(vo.getCompanyName());
        companyEntity.setAdminUser(UserEntityVoMapper.toEntity(vo.getAdminUser()));
        companyEntity.setAgents((Set<UserEntity>) UserEntityVoMapper.toEntity(vo.getAgents()));
        companyEntity.setActive(vo.isActive());
        companyEntity.setQuotas(quotasEntity);
        
        return companyEntity;
    }

    public static List<CompanyVo> toVo(final List<CompanyEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(entity -> toVo(entity))
                .collect(Collectors.toList());
    }

    public static List<CompanyEntity> toEntity(final List<CompanyVo> vos) {
        if (vos == null) {
            return null;
        }

        return vos.stream()
                .map(vo -> toEntity(vo))
                .collect(Collectors.toList());
    }

}
