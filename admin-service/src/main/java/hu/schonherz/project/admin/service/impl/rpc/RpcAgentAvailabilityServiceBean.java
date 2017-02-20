package hu.schonherz.project.admin.service.impl.rpc;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.service.api.rpc.NoAvailableAgentFoundException;
import hu.schonherz.project.admin.service.api.rpc.RpcAgentAvailabilityServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;

@Stateless(mappedName = "RpcAgentAvailabilityService")
@Remote(RpcAgentAvailabilityServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RpcAgentAvailabilityServiceBean implements RpcAgentAvailabilityServiceRemote {
    @EJB
    private CompanyServiceLocal companyServiceLocal;
    @EJB
    private UserServiceLocal userServiceLocal;

    @Override
    public Long getAvailableAgent(final String source) throws NoAvailableAgentFoundException {
        CompanyVo currentCompanyVo = companyServiceLocal.findByDomainAddressContaining(source);
        for (UserVo agent : currentCompanyVo.getAgents()) {
            if (agent.isAvailable()) {
                agent.setAvailable(false);
                userServiceLocal.changeAvailability(agent.getId());
                return agent.getId();
            }
        }
        throw new NoAvailableAgentFoundException("There's no available Agent with " + source + " domain.");
    }

    @Override
    public void SetAgentAvailability(final String username) throws NoAvailableAgentFoundException {
        UserVo userVo = userServiceLocal.findByUsername(username);
        userServiceLocal.changeAvailability(userVo.getId());
    }
}
