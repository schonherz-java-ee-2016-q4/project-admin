package hu.schonherz.project.admin.service.mapper.company;

import hu.schonherz.project.admin.data.entity.CompanyEntity;
import hu.schonherz.project.admin.data.quota.QuotasEntity;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.mapper.user.UserEntityVoMapper;

import java.util.List;
import java.util.stream.Collectors;

public final class CompanyEntityVoMapper {

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
        companyVo.setDomainAddress(entity.getDomainAddress());
        companyVo.setAdminUser(UserEntityVoMapper.toVo(entity.getAdminUser()));
        companyVo.setAgents(UserEntityVoMapper.toVo(entity.getAgents()));
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
        companyEntity.setDomainAddress(vo.getDomainAddress());
        companyEntity.setAdminUser(UserEntityVoMapper.toEntity(vo.getAdminUser()));
        companyEntity.setAgents(UserEntityVoMapper.toEntity(vo.getAgents()));
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
