package hu.schonherz.project.admin.service.impl.user;

import hu.schonherz.project.admin.service.api.rpc.RpcLoginServiceRemote;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

@Stateless(mappedName = "RpcLoginService")
@Remote(UserServiceLocal.class)
@WebService(serviceName = "RpcLogin")
public class RpcLoginServiceBean implements RpcLoginServiceRemote {

    @EJB
    private UserServiceLocal userService;

    @Override
    @WebMethod(operationName = "login")
    public String rpcLogin(String username, String password) {
        UserVo user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("The user " + username + " does not exist!");
        }

        return null;
    }

}
