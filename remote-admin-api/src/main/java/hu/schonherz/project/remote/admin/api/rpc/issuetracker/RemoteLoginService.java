package hu.schonherz.project.remote.admin.api.rpc.issuetracker;

import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;

public interface RemoteLoginService {

    RemoteUserVo login(String username);

}
