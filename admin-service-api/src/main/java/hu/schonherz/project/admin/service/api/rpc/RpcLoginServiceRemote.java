package hu.schonherz.project.admin.service.api.rpc;

import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface RpcLoginServiceRemote {

    UserVo rpcLogin(String username, String plainTextPassword) throws FailedRpcLoginAttemptException;

}
