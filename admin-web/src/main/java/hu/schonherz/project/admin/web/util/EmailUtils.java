package hu.schonherz.project.admin.web.util;

import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Stateful(name = "EmailUtils")
@Slf4j
public class EmailUtils {

    @EJB
    private UserServiceRemote userServiceRemote;

    // User buffers
    private Set<UserVo> profileCandidades;
    private Set<UserVo> registrationCandidates;

    public List<String> completeEmailForProfile(@NonNull final String emailPart, @NonNull final String companyName) {
        initProfileCandidatesIfNeeded(companyName);
        return profileCandidades.stream()
                .map(UserVo::getEmail)
                .filter(email -> email.contains(emailPart))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> completeEmailForRegistration(@NonNull final String emailPart) {
        initRegistrationCandidatesIfNeeded();
        return registrationCandidates.stream()
                .map(UserVo::getEmail)
                .filter(email -> email.contains(emailPart))
                .sorted()
                .collect(Collectors.toList());
    }

    private void initProfileCandidatesIfNeeded(final String companyName) {
        if (profileCandidades == null) {
            profileCandidades = userServiceRemote.findByCompanyName(companyName);
            profileCandidades.addAll(userServiceRemote.findByCompanyName(null));
        }
    }

    private void initRegistrationCandidatesIfNeeded() {
        if (registrationCandidates == null) {
            registrationCandidates = userServiceRemote.findByCompanyName(null);
        }
    }

}
