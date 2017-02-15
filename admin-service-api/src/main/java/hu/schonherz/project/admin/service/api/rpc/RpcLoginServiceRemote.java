package hu.schonherz.project.admin.service.api.rpc;

import hu.schonherz.project.admin.service.api.vo.UserData;

public interface RpcLoginServiceRemote {

    UserData rpcLogin(String username) throws FailedRpcLoginAttemptException;

    void rpcLogout(String username) throws FailedRpcLogoutException;

}
