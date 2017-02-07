package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.service.api.vo.UserData;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class UserDataVoMapper {

    private static final Mapper MAPPER;

    static {
        MAPPER = new DozerBeanMapper();
    }

    private UserDataVoMapper() {
    }

    public static UserVo toVo(@NonNull final UserData userEntity) {
        return MAPPER.map(userEntity, UserVo.class);
    }

    public static UserData toData(@NonNull final UserVo userVo) {
        return MAPPER.map(userVo, UserData.class);
    }

    public static List<UserVo> toVo(@NonNull final Collection<UserData> entities) {
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(entity -> toVo(entity))
                .collect(Collectors.toList());
    }

    public static List<UserData> toData(@NonNull final Collection<UserVo> vos) {
        if (vos.isEmpty()) {
            return new ArrayList<>();
        }

        return vos.stream()
                .map(vo -> toData(vo))
                .collect(Collectors.toList());
    }

}
