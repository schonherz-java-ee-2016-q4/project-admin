package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.service.api.vo.Gender;
import hu.schonherz.project.admin.service.api.vo.UserData;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class UserDataVoMapper {

    private UserDataVoMapper() {
    }

    public static UserData toData(final UserVo userVo) {
        UserData userData = new UserData();
        userData.setId(userVo.getId());
        userData.setUsername(userVo.getUsername());
        userData.setPassword(userVo.getPassword());
        userData.setEmail(userVo.getEmail());
        userData.setUserRole(userVo.getUserRole());

        userData.setCompanyName("Wayne Enterprises, Inc");
        userData.setFullName("Bruce Wayne");
        userData.setPhone("+36-30-1112367");
        userData.setPicUrl("https://pbs.twimg.com/profile_images/649259478332784640/7Pjcfx_v_reasonably_small.jpg");
        userData.setGender(Gender.MALE);

        return userData;
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
