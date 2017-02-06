package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import org.dozer.DozerConverter;

public class UserRoleConverter extends DozerConverter<String, UserRole> {

    public UserRoleConverter(Class<String> prototypeString, Class<UserRole> prototypeUserRole) {
        super(prototypeString, prototypeUserRole);
    }

    @Override
    public String convertFrom(UserRole roleEnum, String roleString) {
        return roleEnum.name();
    }

    @Override
    public UserRole convertTo(String roleString, UserRole roleEnum) {
        return UserRole.fromString(roleString);
    }

}
