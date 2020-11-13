package com.icebartech.base.manager.service;

import com.icebartech.base.manager.dto.Manager;
import com.icebartech.base.manager.po.SysManager;
import com.icebartech.core.modules.BaseService;

public interface ManagerService extends BaseService<Manager, SysManager> {

    String login(String loginName, String password);

    Boolean resetPassowrd(Long id, String oldPassword, String newPassword);

    Boolean resetPwd(Long userId, String newPassword);
}
