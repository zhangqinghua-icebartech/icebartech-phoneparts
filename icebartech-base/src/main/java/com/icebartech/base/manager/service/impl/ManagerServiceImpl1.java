package com.icebartech.base.manager.service.impl;

import com.icebartech.base.manager.dto.Manager;
import com.icebartech.base.manager.dto.Role;
import com.icebartech.base.manager.po.SysManager;
import com.icebartech.base.manager.repository.ManagerRepository;
import com.icebartech.base.manager.service.ManagerService;
import com.icebartech.base.manager.service.MenuService;
import com.icebartech.base.manager.service.RoleService;
import com.icebartech.core.components.LoginComponent;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.EncryptUtil;
import com.icebartech.core.vo.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.icebartech.core.vo.QueryParam.attr;
import static com.icebartech.core.vo.QueryParam.eq;

@Service
@Slf4j
public class ManagerServiceImpl1 extends AbstractService<Manager, SysManager, ManagerRepository> implements ManagerService {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private LoginComponent loginComponent;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ManagerRepository repository;
    @Autowired
    private EncryptUtil encryptUtil;

    @Override
    protected void preInsert(Manager d) {
        if (super.exists(eq(Manager::getLoginName, d.getLoginName()))) {
            throw new ServiceException(CommonResultCodeEnum.INVALID_ARGUMENTS, "此账号已存在");
        }
        // 加密密码
        d.setPassword(passwordEncoder.encode(d.getPassword()));
    }

    @Override
    protected void preUpdate(Manager d) {
        // 加密密码
        if (StringUtils.isNotBlank(d.getPassword())) {
            d.setPassword(passwordEncoder.encode(d.getPassword()));
        }
    }

    @Override
    protected void warpDTO(Manager d) {
        d.setRoleName(roleService.findOne(d.getRoleId()).getRoleName());
    }

    @Override
    protected void warpDetail(Long id, Manager d) {
        // 管理员可见的权限列表
        d.setMenus(menuService.findManagerMenus(id));
    }

    /**
     * 角色删除
     * 判断此角色是否被管理员引用
     */
    public void referenceDelete(Long referenceId, Role reference) {
        if (super.exists(eq(Manager::getRoleId, referenceId))) {
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "此角色被管理员引用，无法删除");
        }
    }

    @Override
    public String login(String loginName, String password) {
        if (StringUtils.isBlank(loginName)) {
            throw new ServiceException(CommonResultCodeEnum.LOGIN_ERROR, "账号不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new ServiceException(CommonResultCodeEnum.LOGIN_ERROR, "密码不能为空");
        }

        // 管理员登录或一级代理商登录
        if (super.exists(eq(Manager::getLoginName, loginName))) {
            // 1. 查询
            Manager manager = BeanMapper.map(repository.findManager(loginName), Manager.class);

            System.out.println(manager);
            if (!passwordEncoder.matches(password, manager.getPassword())) {
                throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");
            }

            // 判断是不是代理商。管理员绑定了代理商就属于代理商身份。
            UserEnum userEnum = manager.getAgentId() == 0L ? UserEnum.admin : UserEnum.agent;
            manager.setId(userEnum == UserEnum.admin ? manager.getId() : manager.getAgentId());
            int level = userEnum == UserEnum.admin ? 0  :1;


            LocalUser localUser = new LocalUser();
            localUser.setSessionId(loginComponent.composeSessionId(userEnum, manager.getId()));
            localUser.setParentId(0L);
            localUser.setLevel(level);
            localUser.setUserId(manager.getId());
            localUser.setUserEnum(userEnum);
            loginComponent.login(localUser.getSessionId(), localUser, 7 * 24 * 60 * 60);

            return localUser.getSessionId();
        }

        // 二级代理商登录
        Map<String, Object> agentMap = repository.findAgent(loginName);
        String pwd = agentMap.containsKey("pwd") ? (String) agentMap.get("pwd") : null;
        Long id = agentMap.containsKey("id") ? Long.valueOf(String.valueOf(agentMap.get("id"))) : null;


        Long parentId = agentMap.containsKey("parentId") ? Long.valueOf(String.valueOf(agentMap.get("parentId"))) : null;
        int level = parentId == null || 0L == parentId ? 1 : 2;
        String enable = agentMap.containsKey("enable") ? agentMap.get("enable").toString() : null;


        if (id == null)
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if (enable == null || enable.equals("y"))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "运营商已被禁用");

        if (pwd == null || !password.equals(encryptUtil.Base64Decode(pwd)))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        LocalUser localUser = new LocalUser();
        localUser.setSessionId(loginComponent.composeSessionId(UserEnum.agent, id));
        localUser.setParentId(parentId);
        localUser.setLevel(level);
        localUser.setUserId(id);
        localUser.setUserEnum(UserEnum.agent);
        loginComponent.login(localUser.getSessionId(), localUser, 7 * 24 * 60 * 60);
        return localUser.getSessionId();
    }

    @Override
    public Boolean resetPassowrd(Long id, String oldPassword, String newPassword) {
        Manager manager = super.findOne(id);
        if (!passwordEncoder.matches(oldPassword, manager.getPassword())) {
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "旧密码错误");
        }

        return super.update(QueryParam.id(id), attr(Manager::getPassword, newPassword));
    }

    @Override
    public Boolean resetPwd(Long userId, String newPassword) {
        return super.update(QueryParam.id(userId), attr(Manager::getPassword, newPassword));
    }
}
