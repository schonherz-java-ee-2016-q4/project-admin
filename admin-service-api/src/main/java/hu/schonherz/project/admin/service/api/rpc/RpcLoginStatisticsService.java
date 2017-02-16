package hu.schonherz.project.admin.service.api.rpc;

import java.time.LocalDateTime;
import java.util.List;

public interface RpcLoginStatisticsService {

    List<LocalDateTime> getAllLoginsOf(String username) throws LoginDataRetrievalException;

}
