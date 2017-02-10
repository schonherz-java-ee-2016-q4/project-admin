package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.service.api.vo.UserData;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public final class UserDataVoMapper {

    private static final Mapper MAPPER;

    static {
        MAPPER = new DozerBeanMapper();
    }

    private UserDataVoMapper() {
    }

    public static UserVo toVo(final UserData userData) {
        if (userData == null) {
            return null;
        }

        return MAPPER.map(userData, UserVo.class);
    }

    public static UserData toData(final UserVo userVo) {
        if (userVo == null) {
            return null;
        }

        return MAPPER.map(userVo, UserData.class);
    }

    public static List<UserVo> toVo(final Collection<UserData> datas) {
        if (datas == null) {
            return null;
        }

        if (datas.isEmpty()) {
            return new ArrayList<>();
        }

        return datas.stream()
                .map(entity -> toVo(entity))
                .collect(Collectors.toList());
    }

    public static List<UserData> toData(final Collection<UserVo> vos) {
        if (vos == null) {
            return null;
        }

        if (vos.isEmpty()) {
            return new ArrayList<>();
        }

        return vos.stream()
                .map(vo -> toData(vo))
                .collect(Collectors.toList());
    }

}
