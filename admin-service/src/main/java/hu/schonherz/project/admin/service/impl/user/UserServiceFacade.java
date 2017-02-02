package hu.schonherz.project.admin.service.impl.user;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.service.exception.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserVo;

@Stateless(mappedName = "UserServiceFacade")
@Remote(UserServiceRemote.class)
public class UserServiceFacade implements UserServiceRemote {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceFacade.class);

    @EJB
    private UserServiceLocal realService;

    @Override
    public UserVo findByUsername(final String username) {
        return realService.findByUsername(username);
    }

    @Override
    public UserVo registrationUser(final UserVo userVo) throws InvalidUserDataException {
        try {
            return realService.registrationUser(userVo);
        } catch (EJBTransactionRolledbackException rolledBackException) {
            throw new InvalidUserDataException("Could not save user!", rolledBackException);
        }
    }

    @Override
    public List<UserVo> findAll() {
        return realService.findAll();
    }

}
