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
        initMockUserVo(userVo);
        UserData userData = new UserData();
        userData.setId(userVo.getId());
        userData.setUsername(userVo.getUsername());
        userData.setPassword(userVo.getPassword());
        userData.setEmail(userVo.getEmail());
        userData.setUserRole(userVo.getUserRole());

        userData.setCompanyName(userVo.getCompanyName());
        userData.setFullName(userVo.getFullName());
        userData.setPhone(userVo.getPhone());
        userData.setPicUrl(userVo.getPicUrl());
        userData.setGender(userVo.getGender());

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


    private static void initMockUserVo(final UserVo userVo) {
        userVo.setFullName("Bruce Wayne asassa");
        userVo.setGender(Gender.MALE);
        userVo.setPhone("+36-30-1112367");
        userVo.setPicUrl("https://pbs.twimg.com/profile_images/649259478332784640/7Pjcfx_v_reasonably_small.jpg");
    }
}
