package hu.schonherz.project.admin.service.impl.company;

import hu.schonherz.project.admin.service.api.service.company.CompanyServiceLocal;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompanyServiceFacade implements CompanyServiceRemote {

    @EJB
    private CompanyServiceLocal realService;

    @Override
    public CompanyVo save(CompanyVo companyVo) throws InvalidCompanyDataException {
        try {
            return realService.save(companyVo);
        } catch (EJBTransactionRolledbackException rolledBackException) {
            throw new InvalidCompanyDataException("Could not save company: ", rolledBackException);
        }
    }

    @Override
    public CompanyVo findById(Long id) {
        return realService.findById(id);
    }

    @Override
    public CompanyVo findByName(String companyName) {
        return realService.findByName(companyName);
    }

    @Override
    public List<CompanyVo> findAll() {
        return realService.findAll();
    }

    @Override
    public void changeStatus(Long id) {
        CompanyVo companyVo = realService.findById(id);
        if (companyVo != null) {
            companyVo.setActive(!companyVo.isActive());
            log.info("Company " + companyVo.getCompanyName() + " is now " + (companyVo.isActive() ? "active" : "inactive"));
        } else {
            log.warn("The company with id " + id + " does not exist. Cannot change status.");
        }
    }

}
