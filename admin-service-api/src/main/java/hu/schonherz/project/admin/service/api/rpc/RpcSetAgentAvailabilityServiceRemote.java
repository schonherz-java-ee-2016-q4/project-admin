package hu.schonherz.project.admin.service.api.rpc;

public interface RpcSetAgentAvailabilityServiceRemote {
    void SetAgentAvailability(String username) throws NoAvailableAgentFoundException;
}
