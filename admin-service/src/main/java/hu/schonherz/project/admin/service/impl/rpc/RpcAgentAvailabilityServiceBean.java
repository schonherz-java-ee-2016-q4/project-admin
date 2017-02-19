package hu.schonherz.project.admin.service.impl.rpc;

import javax.ejb.EJB;

import org.springframework.beans.factory.annotation.Autowired;

import hu.schonherz.project.admin.data.repository.CompanyRepository;
import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.rpc.NoAvailableAgentFoundException;
import hu.schonherz.project.admin.service.api.rpc.RpcAgentAvailabilityServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.company.CompanyEntityVoMapper;
import hu.schonherz.project.admin.service.mapper.user.UserEntityVoMapper;

public class RpcAgentAvailabilityServiceBean implements RpcAgentAvailabilityServiceRemote {
    @Autowired
    private CompanyRepository companyRepository;
    @EJB
    private CompanyServiceLocal companyServiceLocal;
    @Autowired
    private UserRepository userRepository;
    @EJB
    private UserServiceLocal userServiceLocal;

    @Override
    public Long getAvailableAgent(final String source) throws NoAvailableAgentFoundException {
        CompanyVo currentCompanyVo = CompanyEntityVoMapper.toVo(companyRepository.findByDomainAddressContaining(source));
        for (UserVo agent : currentCompanyVo.getAgents()) {
            if (agent.isAvailable()) {
                agent.setAvailable(false);
                return agent.getId();
            }
        }
        throw new NoAvailableAgentFoundException("There's no available Agent with " + source + " domain.");
    }

    @Override
    public void SetAgentAvailability(final String username) throws NoAvailableAgentFoundException {
        UserVo userVo = UserEntityVoMapper.toVo(userRepository.findByUsername(username));
        userVo.setAvailable(true);
        userRepository.save(UserEntityVoMapper.toEntity(userVo));
    }
}
