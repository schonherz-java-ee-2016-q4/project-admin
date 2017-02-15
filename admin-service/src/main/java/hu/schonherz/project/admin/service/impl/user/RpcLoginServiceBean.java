package hu.schonherz.project.admin.service.impl.user;

import hu.schonherz.project.admin.service.api.login.LoginService;
import hu.schonherz.project.admin.service.api.rpc.FailedRpcLoginAttemptException;
import hu.schonherz.project.admin.service.api.rpc.FailedRpcLogoutException;
import hu.schonherz.project.admin.service.api.rpc.RpcLoginServiceRemote;
import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.LoginVo;
import hu.schonherz.project.admin.service.api.vo.UserData;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.user.UserDataVoMapper;
import java.time.LocalDateTime;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Stateless(mappedName = "RpcLoginService")
@Remote(RpcLoginServiceRemote.class)
@Slf4j
public class RpcLoginServiceBean implements RpcLoginServiceRemote {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private LoginService loginService;

    @Override
    public UserData rpcLogin(@NonNull final String username) throws FailedRpcLoginAttemptException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty string!");
        }

        UserVo user = userService.findByUsername(username);
        if (user == null) {
            throw new FailedRpcLoginAttemptException("The user " + username + " does not exist!");
        }

        if (!user.isActive()) {
            throw new FailedRpcLoginAttemptException("User " + username + " is inactive!");
        }

        user.setLoggedIn(true);
        user.setAvailable(true);
        userService.registrationUser(user);
        saveLoginData(user.getId());
        log.info("Succesful remote login. User: {} is available now", username);

        return UserDataVoMapper.toData(user);
    }

    @Override
    public void rpcLogout(@NonNull final String username) throws FailedRpcLogoutException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty string!");
        }

        UserVo user = userService.findByUsername(username);
        if (user == null) {
            throw new FailedRpcLogoutException("The user " + username + " does not exist!");
        }

        if (!user.isLoggedIn()) {
            throw new FailedRpcLogoutException("User " + username + " is not logged in!");
        }

        user.setAvailable(false);
        user.setLoggedIn(false);
        userService.registrationUser(user);
        log.info("Successful remote logout for user {}", username);
    }

    private void saveLoginData(Long userId) {
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(userId);
        loginVo.setLoginDate(LocalDateTime.now());

        loginService.save(loginVo);
    }

}
