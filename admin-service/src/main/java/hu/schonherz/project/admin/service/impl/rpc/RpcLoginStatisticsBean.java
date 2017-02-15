package hu.schonherz.project.admin.service.impl.rpc;

import hu.schonherz.project.admin.service.api.login.LoginService;
import hu.schonherz.project.admin.service.api.rpc.LoginDataRetrievalException;
import hu.schonherz.project.admin.service.api.rpc.RpcLoginStatisticsService;
import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.LoginVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Stateless(mappedName = "RpcLoginStatisticsService")
@Remote(RpcLoginStatisticsService.class)
@Slf4j
public class RpcLoginStatisticsBean implements RpcLoginStatisticsService {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private LoginService loginService;

    @Override
    public List<LocalDateTime> getAllLoginsOf(@NonNull final String username) throws LoginDataRetrievalException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty string!");
        }

        UserVo user = userService.findByUsername(username);
        if (user == null) {
            throw new LoginDataRetrievalException("User with username " + username + " does not exist!");
        }

        List<LoginVo> logins = loginService.findByUserId(user.getId());

        return logins.stream()
                .map(login -> login.getLoginDate())
                .collect(Collectors.toList());
    }

}
