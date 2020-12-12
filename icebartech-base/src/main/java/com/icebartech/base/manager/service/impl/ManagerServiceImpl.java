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
import com.icebartech.core.utils.EncryptUtil;
import com.icebartech.core.vo.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.*;

@Service
@Slf4j
public class ManagerServiceImpl extends AbstractService<Manager, SysManager, ManagerRepository> implements ManagerService {

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
    protected void warpDTO(List<Long> ids, List<Manager> ds) {
        List<Role> roles = roleService.findList(in(Role::getId, ds.stream().map(Manager::getRoleId).collect(Collectors.toList())));
        ds.forEach(d->d.setRoleName(roles.stream().filter(r->r.getId().equals(d.getRoleId())).map(Role::getRoleName).findAny().orElse(null)));
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
    public String adminLogin(String loginName, String password) {

        UserEnum userEnum = UserEnum.agent;//角色
        String pwd;//密码
        Long id;//登录人id
        Long parentId = null;//代理商上级id
        int level;//代理商等级

        //管理员登录
        Manager manager = super.findOneOrNull(eq(Manager::getLoginName, loginName));
        if(manager != null){
            pwd = manager.getPassword();
            id = manager.getId();
            if(manager.getAgentId()==0L) {
                userEnum = UserEnum.admin;
                level = 0;
            }
            else{
                id = manager.getAgentId();
                level = 1;
            }

        }
        //运营商登录
        else {
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");
        }
        if(id == null || pwd == null)
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if(userEnum == UserEnum.admin && !passwordEncoder.matches(password, pwd))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if(userEnum == UserEnum.agent && !password.equals(encryptUtil.Base64Decode(pwd)))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        log.info("登录 login = {},id = {},UserEnum = {}",loginName,id,userEnum.name());

        String sessionId = loginComponent.composeSessionId(userEnum, id);
        LocalUser localUser = new LocalUser();
        localUser.setSessionId(sessionId);
        localUser.setParentId(parentId);
        localUser.setLevel(level);
        localUser.setUserId(id);
        localUser.setUserEnum(userEnum);
        loginComponent.login(sessionId, localUser,7*24*60*60);
        return sessionId;

    }

    @Override
    public String agentLogin(String loginName, String password) {

        UserEnum userEnum = UserEnum.agent;//角色
        String pwd;//密码
        Long id;//登录人id
        Long parentId = null;//代理商上级id
        int level;//代理商等级

        //管理员登录
        Manager manager = super.findOneOrNull(eq(Manager::getLoginName, loginName));
        if(manager != null){
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");
        }
        //运营商登录
        else {
            Map<String,Object> agentMap = repository.findAgent(loginName);
            pwd = agentMap.containsKey("pwd")?(String) agentMap.get("pwd"):null;
            id = agentMap.containsKey("id")?Long.valueOf(String.valueOf(agentMap.get("id"))):null;
            parentId = agentMap.containsKey("parentId")?Long.valueOf(String.valueOf(agentMap.get("parentId"))):null;
            String enable = agentMap.containsKey("enable")? agentMap.get("enable").toString() :null;
            if(enable==null||enable.equals("y"))
                throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "运营商已被禁用");

            if(parentId==null|| 0L == parentId){
                level = 1;
            }else {
                level = 2;
            }
        }
        if(id == null || pwd == null)
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if(userEnum == UserEnum.admin && !passwordEncoder.matches(password, pwd))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if(userEnum == UserEnum.agent && !password.equals(encryptUtil.Base64Decode(pwd)))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        log.info("登录 login = {},id = {},UserEnum = {}",loginName,id,userEnum.name());

        String sessionId = loginComponent.composeSessionId(userEnum, id);
        LocalUser localUser = new LocalUser();
        localUser.setSessionId(sessionId);
        localUser.setParentId(parentId);
        localUser.setLevel(level);
        localUser.setUserId(id);
        localUser.setUserEnum(userEnum);
        loginComponent.login(sessionId, localUser,7*24*60*60);
        return sessionId;
    }

    @Override
    public String login(String loginName, String password) {

        UserEnum userEnum = UserEnum.agent;//角色
        String pwd;//密码
        Long id;//登录人id
        Long parentId = null;//代理商上级id
        int level;//代理商等级

        //管理员登录
        Manager manager = super.findOneOrNull(eq(Manager::getLoginName, loginName));
        if(manager != null){
            pwd = manager.getPassword();
            id = manager.getId();
            if(manager.getAgentId()==0L) {
                userEnum = UserEnum.admin;
                level = 0;
            }
            else{
                id = manager.getAgentId();
                level = 1;
            }

        }
        //运营商登录
        else {
            Map<String,Object> agentMap = repository.findAgent(loginName);
            pwd = agentMap.containsKey("pwd")?(String) agentMap.get("pwd"):null;
            id = agentMap.containsKey("id")?Long.valueOf(String.valueOf(agentMap.get("id"))):null;
            parentId = agentMap.containsKey("parentId")?Long.valueOf(String.valueOf(agentMap.get("parentId"))):null;
            String enable = agentMap.containsKey("enable")? agentMap.get("enable").toString() :null;
            if(enable==null||enable.equals("y"))
                throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "运营商已被禁用");

            if(parentId==null|| 0L == parentId){
                level = 1;
            }else {
                level = 2;
            }
        }
        if(id == null || pwd == null)
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if(userEnum == UserEnum.admin && !passwordEncoder.matches(password, pwd))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        if(userEnum == UserEnum.agent && !password.equals(encryptUtil.Base64Decode(pwd)))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, "账号或密码错误");

        log.info("登录 login = {},id = {},UserEnum = {}",loginName,id,userEnum.name());

        String sessionId = loginComponent.composeSessionId(userEnum, id);
        LocalUser localUser = new LocalUser();
        localUser.setSessionId(sessionId);
        localUser.setParentId(parentId);
        localUser.setLevel(level);
        localUser.setUserId(id);
        localUser.setUserEnum(userEnum);
        loginComponent.login(sessionId, localUser,7*24*60*60);
        return sessionId;
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
