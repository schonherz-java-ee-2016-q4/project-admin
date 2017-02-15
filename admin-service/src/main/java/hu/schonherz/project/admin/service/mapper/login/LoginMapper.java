package hu.schonherz.project.admin.service.mapper.login;

import hu.schonherz.project.admin.data.entity.LoginEntity;
import hu.schonherz.project.admin.service.api.vo.LoginVo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public final class LoginMapper {

    private static final Mapper MAPPER;

    static {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        List<String> mappingFiles = new ArrayList<>();
        mappingFiles.add("dozerJdk8Converters.xml");

        dozerBeanMapper.setMappingFiles(mappingFiles);
        MAPPER = dozerBeanMapper;
    }

    private LoginMapper() {
    }

    public static LoginVo toVo(final LoginEntity login) {
        if (login == null) {
            return null;
        }
        return MAPPER.map(login, LoginVo.class);
    }

    public static LoginEntity toEntity(final LoginVo loginVO) {
        if (loginVO == null) {
            return null;
        }
        return MAPPER.map(loginVO, LoginEntity.class);
    }

    public static List<LoginVo> toVo(final List<LoginEntity> logins) {
        if (logins == null) {
            return null;
        }

        return logins.stream()
                .map(entity -> LoginMapper.toVo(entity))
                .collect(Collectors.toList());
    }

    public static List<LoginEntity> toEntity(final List<LoginVo> loginsVO) {
        if (loginsVO == null) {
            return null;
        }

        return loginsVO.stream()
                .map(vo -> toEntity(vo))
                .collect(Collectors.toList());
    }
}
