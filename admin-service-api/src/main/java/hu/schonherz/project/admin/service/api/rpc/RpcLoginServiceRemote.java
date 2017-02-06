package hu.schonherz.project.admin.service.api.rpc;

public interface RpcLoginServiceRemote {

    String rpcLogin(String username, String plainTextPassword) throws FailedRpcLoginAttemptException;

}
