package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.service.api.vo.UserVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class UserVoMapper {

    static Mapper mapper = new DozerBeanMapper();

    private UserVoMapper() {
    }

    public static UserVo toVo(@NonNull UserEntity userEntity) {
        return mapper.map(userEntity, UserVo.class);
    }

    public static UserEntity toEntity(@NonNull UserVo userVo) {
        return mapper.map(userVo, UserEntity.class);
    }

    public static List<UserVo> toVo(@NonNull Collection<UserEntity> entities) {
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(entity -> toVo(entity))
                .collect(Collectors.toList());
    }

    public static List<UserEntity> toEntity(@NonNull Collection<UserVo> vos) {
        if (vos.isEmpty()) {
            return new ArrayList<>();
        }

        return vos.stream()
                .map(vo -> toEntity(vo))
                .collect(Collectors.toList());
    }

}
