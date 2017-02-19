package hu.schonherz.project.admin.service.api.rpc;

public interface RpcAgentAvailabilityServiceRemote {
    Long getAvailableAgent(String source) throws NoAvailableAgentFoundException;
    
    void SetAgentAvailability(String username) throws NoAvailableAgentFoundException;
    }
