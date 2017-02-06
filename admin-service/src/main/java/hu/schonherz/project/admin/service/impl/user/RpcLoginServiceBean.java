package hu.schonherz.project.admin.service.impl.user;

import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.rpc.FailedRpcLoginAttemptException;
import hu.schonherz.project.admin.service.api.rpc.RpcLoginServiceRemote;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import lombok.NonNull;

@Stateless(mappedName = "RpcLoginService")
@Remote(UserServiceLocal.class)
@WebService(serviceName = "RpcLogin")
public class RpcLoginServiceBean implements RpcLoginServiceRemote {

    @EJB
    private UserServiceLocal userService;

    @Override
    @WebMethod(operationName = "login")
    public String rpcLogin(@NonNull String username, @NonNull String plainTextPassword) throws FailedRpcLoginAttemptException {
        if (username.isEmpty() || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be empty string!");
        }

        UserVo user = userService.findByUsername(username);
        if (user == null) {
            throw new FailedRpcLoginAttemptException("The user " + username + " does not exist!");
        }

        String encryptedGivenPassword = Encrypter.encrypt(plainTextPassword);
        String encryptedUserPassword = user.getPassword();

        if (!Encrypter.match(encryptedUserPassword, encryptedGivenPassword)) {
            throw new FailedRpcLoginAttemptException("Invalid password!");
        }

        if (!user.isActive()) {
            throw new FailedRpcLoginAttemptException("User " + username + " is inactive!");
        }

        return user.getUserRole();
    }

}
