package hu.schonherz.project.admin.service.impl.user;

import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(mappedName = "UserServiceFacade")
@Remote(UserServiceRemote.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserServiceFacade implements UserServiceRemote {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceFacade.class);

    @EJB
    private UserServiceLocal realService;

    @Override
    public UserVo findByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UserVo registrationUser(UserVo userVo) {
        try {
            return realService.registrationUser(userVo);
        } catch (Throwable t) {
            LOG.error("----- Exception was caught in the facade! -----");
            return null;
        }
    }

    @Override
    public List<UserVo> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
