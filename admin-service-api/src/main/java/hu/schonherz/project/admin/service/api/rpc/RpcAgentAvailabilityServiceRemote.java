package hu.schonherz.project.admin.service.api.rpc;

public interface RpcAgentAvailabilityServiceRemote {
    Long getAvailableAgent(String source) throws NoAvailableAgentFoundException;
}
