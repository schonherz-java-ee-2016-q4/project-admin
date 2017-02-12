package hu.schonherz.project.admin.service.mapper.company;

import hu.schonherz.project.admin.data.entity.CompanyEntity;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import java.util.List;
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

        return MAPPER.map(CompanyEntity.class, CompanyVo.class);
    }

    public static CompanyEntity toEntity(final CompanyVo vo) {
        if (vo == null) {
            return null;
        }

        return MAPPER.map(CompanyVo.class, CompanyEntity.class);
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
