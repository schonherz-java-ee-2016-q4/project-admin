package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.data.entity.TestEntity;
import hu.schonherz.project.admin.service.api.vo.TestUserVo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class TestUserVoMapper {

    static Mapper mapper = new DozerBeanMapper();

    private TestUserVoMapper() {
    }

    public static TestUserVo toVo(@NonNull TestEntity userEntity) {
        return mapper.map(userEntity, TestUserVo.class);
    }

    public static TestEntity toEntity(@NonNull TestUserVo userVo) {
        return mapper.map(userVo, TestEntity.class);
    }

    public static List<TestUserVo> toVo(@NonNull Collection<TestEntity> entities) {
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(entity -> toVo(entity))
                .collect(Collectors.toList());
    }

    public static List<TestEntity> toEntity(@NonNull Collection<TestUserVo> vos) {
        if (vos.isEmpty()) {
            return new ArrayList<>();
        }

        return vos.stream()
                .map(vo -> toEntity(vo))
                .collect(Collectors.toList());
    }

}
