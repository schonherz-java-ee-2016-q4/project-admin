package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import lombok.NonNull;
import org.dozer.DozerConverter;

public class UserRoleConverter extends DozerConverter<String, UserRole> {

    public UserRoleConverter(@NonNull final Class<String> prototypeString, @NonNull final Class<UserRole> prototypeUserRole) {
        super(prototypeString, prototypeUserRole);
    }

    @Override
    public String convertFrom(@NonNull final UserRole roleEnum, @NonNull final String roleString) {
        return roleEnum.name();
    }

    @Override
    public UserRole convertTo(@NonNull final String roleString, @NonNull final UserRole roleEnum) {
        return UserRole.fromString(roleString);
    }

}
