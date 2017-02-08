package hu.schonherz.project.admin.service.impl.user;

import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.rpc.FailedRpcLoginAttemptException;
import hu.schonherz.project.admin.service.api.rpc.RpcLoginServiceRemote;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserData;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.user.UserDataVoMapper;
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

    @Override
    public UserData rpcLogin(@NonNull final String username, @NonNull final String plainTextPassword) throws FailedRpcLoginAttemptException {
        if (username.isEmpty() || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be empty string!");
        }

        UserVo user = userService.findByUsername(username);
        log.warn("---------------------------- user: " + user);
        if (user == null) {
            throw new FailedRpcLoginAttemptException("The user " + username + " does not exist!");
        }

        String encryptedGivenPassword = Encrypter.encrypt(plainTextPassword);
        String encryptedUserPassword = user.getPassword();
        log.warn("------------- encryptedGivenPassword: " + encryptedGivenPassword);
        log.warn("------------- encryptedUserPassword: " + encryptedUserPassword);

        if (!Encrypter.match(encryptedUserPassword, encryptedGivenPassword)) {
            throw new FailedRpcLoginAttemptException("Invalid password!");
        }

        if (!user.isActive()) {
            throw new FailedRpcLoginAttemptException("User " + username + " is inactive!");
        }

        return UserDataVoMapper.toData(user);
    }

}
