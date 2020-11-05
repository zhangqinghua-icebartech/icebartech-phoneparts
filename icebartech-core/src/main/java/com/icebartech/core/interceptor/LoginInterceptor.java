package com.icebartech.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.components.LoginComponent;
import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.constants.IcebartechConstants;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.utils.ResponseUtils;
import com.icebartech.core.vo.BaseData;
import com.icebartech.core.vo.RespJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录拦截器，可以改成其他的权限校验框架
 *
 * @author haosheng.wenhs
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private LoginComponent loginComponent;

    @Autowired
    StringEncryptor stringEncryptor;

    /**
     * 清理资源，清理threadLocal
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object controller = handlerMethod.getBean();

            final RequireLogin requireLogin = handlerMethod.getMethodAnnotation(RequireLogin.class);
            if (controller instanceof BaseController) {
                if (requireLogin != null) {
                    UserThreadLocal.removeUserInfo();
                }
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String disable = (String) redisComponent.get(IcebartechConstants.SYSTEM_DISABLE, IcebartechConstants.SYSTEM_DISABLE);
        if ("y".equals(disable) && !"/api/base/sys/admin/setDisable".equals(request.getRequestURI()) && !"/base/sys/admin/setDisable".equals(request.getRequestURI())) {
            //返回json结果
            renderServiceError(response);
            return false;
        }
        if (handler instanceof ResourceHttpRequestHandler) {
            // 静态资源不做权限处理
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object controller = handlerMethod.getBean();
        if (controller instanceof BaseController) {
            BaseController baseController = (BaseController) controller;
            baseController.setRequest(request);
            baseController.setResponse(response);
        }

        Set<UserEnum> userEnums = new HashSet<>();
        Set<UserEnum> ignores = new HashSet<>();
        // 方法上的RequireLogin注解
        RequireLogin methodAnnotation = handlerMethod.getMethodAnnotation(RequireLogin.class);
        if (null != methodAnnotation) {
            userEnums.addAll(Arrays.asList(methodAnnotation.userEnum()));
            userEnums.addAll(Arrays.asList(methodAnnotation.value()));
            ignores.addAll(Arrays.asList(methodAnnotation.ignore()));
        }
        // 类上的RequireLogin注解
        RequireLogin classAnnotation = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
        if (null != classAnnotation) {
            userEnums.addAll(Arrays.asList(classAnnotation.userEnum()));
            userEnums.addAll(Arrays.asList(classAnnotation.value()));
            ignores.addAll(Arrays.asList(classAnnotation.ignore()));
        }
        // 清除要忽略的身份
        userEnums.removeAll(ignores);
        if (CollectionUtils.isEmpty(userEnums)) {
            return true;
        }
        String sessionId = request.getHeader("sessionId");
        String sessionIdKey;

        if (userEnums.contains(UserEnum.no_login)) {
            // 无需登录 设定空用户对象并放行 return true
            LocalUser localUser= new LocalUser();
            localUser.setLevel(0);
            localUser.setUserId(0L);
            UserThreadLocal.setUserInfo(localUser);
            return true;
        }

        if (StringUtils.isEmpty(sessionId)) {
            // sessionId为空且接口有权限拦截 return false
            renderNotLogin(response);
            return false;
        }
        try {
            sessionIdKey  = stringEncryptor.decrypt(sessionId);
        } catch (EncryptionOperationNotPossibleException e) {
            // sessionId解密失败
            renderNotLogin(response);
            return false;
        }
        LocalUser user = (LocalUser) redisComponent.get(IcebartechConstants.USER_SESSION_GROUP_KEY, sessionIdKey);
        if (null == user) {
            // 未找到sessionId对应的用户 return false
            renderNotLogin(response);
            return false;
        }
        if (!userEnums.contains(user.getUserEnum())) {
            // sessionId所属用户身份和接口所需用户身份不符 return false
            renderNotAuthority(response);
            return false;
        }

        //刷新过期
        loginComponent.login(request.getHeader("sessionId"), user,user.getTime());
        UserThreadLocal.setUserInfo(user);
        return true;
    }

    private void renderNotLogin(HttpServletResponse response) {
        RespJson respJson = new RespJson();
        respJson.setStatus(IcebartechConstants.RESULT_STATUS_FAILED);
        respJson.setCode(IcebartechConstants.RESULT_NOT_LOGIN);
        respJson.setData(new BaseData<>(CommonResultCodeEnum.NOT_LOGIN));
        ResponseUtils.renderJson(response, JSON.toJSONString(respJson));
    }

    private void renderNotAuthority(HttpServletResponse response) {
        RespJson respJson = new RespJson();
        respJson.setStatus(IcebartechConstants.RESULT_STATUS_FAILED);
        respJson.setCode(IcebartechConstants.RESULT_NOT_AUTHORITY);
        respJson.setData(new BaseData<>(CommonResultCodeEnum.NOT_AUTHORITY));
        respJson.setStatus("您还没有访问该接口的权限！");
        ResponseUtils.renderJson(response, JSON.toJSONString(respJson));
    }

    private void renderServiceError(HttpServletResponse response) {
        RespJson respJson = new RespJson();
        respJson.setStatus(IcebartechConstants.RESULT_STATUS_FAILED);
        respJson.setCode(IcebartechConstants.SERVICE_ERROR);
        respJson.setData(new BaseData<>(CommonResultCodeEnum.SERVICE_CURRENTLY_UNAVAILABLE));
        ResponseUtils.renderJson(response, JSON.toJSONString(respJson));
    }
}
