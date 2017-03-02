package hu.schonherz.project.admin.service.impl.rpc.issuetracker;

import hu.schonherz.project.admin.service.api.service.user.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteLoginService;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Stateless(mappedName = "RemoteLoginServiceBean")
@Remote(RemoteLoginService.class)
@Slf4j
public class RemoteLoginServiceBean implements RemoteLoginService {

    @EJB
    private UserServiceLocal userService;

    @Override
    public RemoteUserVo login(@NonNull final String username) {
        UserVo user = userService.findByUsername(username);
        if (user == null) {
            log.warn("Not existing user tried to remote login: {}", username);
            return null;
        }

        RemoteUserVo remoteVo = new RemoteUserVo();
        remoteVo.setUsername(username);
        remoteVo.setEncryptedPassword(user.getPassword());
        remoteVo.setEmployerCompanyName(user.getCompanyName());
        remoteVo.setRole(user.getUserRole().name());

        log.info("Successful remote login of user: {}", username);
        return remoteVo;
    }

}
