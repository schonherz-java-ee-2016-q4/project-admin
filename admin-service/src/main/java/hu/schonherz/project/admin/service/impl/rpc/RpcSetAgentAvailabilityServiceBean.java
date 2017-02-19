package hu.schonherz.project.admin.service.impl.rpc;

import javax.ejb.EJB;

import org.springframework.beans.factory.annotation.Autowired;

import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.rpc.NoAvailableAgentFoundException;
import hu.schonherz.project.admin.service.api.rpc.RpcSetAgentAvailabilityServiceRemote;
import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.user.UserEntityVoMapper;

public class RpcSetAgentAvailabilityServiceBean implements RpcSetAgentAvailabilityServiceRemote {
	@Autowired
	private UserRepository userRepository;
	@EJB
	private UserServiceLocal userServiceLocal;
	@Override
	public void SetAgentAvailability(final String username) throws NoAvailableAgentFoundException {
		UserVo userVo = UserEntityVoMapper.toVo(userRepository.findByUsername(username));
		userVo.setAvailable(true);
		userRepository.save(UserEntityVoMapper.toEntity(userVo));
	}
}
