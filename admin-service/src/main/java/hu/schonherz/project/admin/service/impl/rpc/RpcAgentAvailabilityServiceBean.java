package hu.schonherz.project.admin.service.impl.rpc;

import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.service.api.rpc.NoAvailableAgentFoundException;
import hu.schonherz.project.admin.service.api.rpc.NoSuchDomainException;
import hu.schonherz.project.admin.service.api.rpc.RpcAgentAvailabilityServiceRemote;
import hu.schonherz.project.admin.service.api.rpc.UsernameNotFoundException;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Stateless(mappedName = "RpcAgentAvailabilityService")
@Remote(RpcAgentAvailabilityServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RpcAgentAvailabilityServiceBean implements RpcAgentAvailabilityServiceRemote {
    @EJB
    private CompanyServiceLocal companyServiceLocal;
    @EJB
    private UserServiceLocal userServiceLocal;

    @Override
    public Long getAvailableAgent(final String source) throws NoAvailableAgentFoundException, NoSuchDomainException {
        CompanyVo currentCompanyVo = companyServiceLocal.findByDomainAddressContaining(source);
        if (currentCompanyVo == null) {
            throw new NoSuchDomainException("No such Domain Address in Company Domain Addresses.");
        }
        log.info("Company with domain:{}:  {}", source, currentCompanyVo);
        Set<UserVo> agents = currentCompanyVo.getAgents();
        log.info("Current Company Agents: {}", agents);
        for (UserVo agent : currentCompanyVo.getAgents()) {
            if (agent.isAvailable()) {
                userServiceLocal.changeAvailability(agent.getId(), false);
                return agent.getId();
            }
        }

        throw new NoAvailableAgentFoundException("There's no available Agent with " + source + " domain.");
    }

    @Override
    public void setAgentAvailabilityToTrue(final String username) throws UsernameNotFoundException {
        UserVo userVo = userServiceLocal.findByUsername(username);
        if (userVo == null) {
            throw new UsernameNotFoundException("Username not found: {}", username);
        }
        userServiceLocal.changeAvailability(userVo.getId(), true);
    }
}
