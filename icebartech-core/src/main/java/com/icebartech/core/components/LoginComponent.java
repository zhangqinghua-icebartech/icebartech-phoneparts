package com.icebartech.core.components;

import com.icebartech.core.constants.IcebartechConstants;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.properties.SiteProperties;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * 登录组件类
 *
 * @author wenhsh
 */
@Component
public class LoginComponent {

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private SiteProperties siteProperties;

    @Autowired
    StringEncryptor stringEncryptor;

    public String composeSessionId(UserEnum userEnum, long userId) {
        return stringEncryptor.encrypt(userEnum.name() + ":" + userId + ":" + UUID.randomUUID().toString());
    }

    /**
     * 用户登录
     *
     * @param user 用户基本信息对象
     */
    public void login(String sessionId, LocalUser user,long time) {
        redisComponent.set(IcebartechConstants.USER_SESSION_GROUP_KEY, stringEncryptor.decrypt(sessionId), user,time);

        //刷新redis的session过期时间
        redisComponent.expire(IcebartechConstants.USER_SESSION_GROUP_KEY, stringEncryptor.decrypt(sessionId), siteProperties.getSessionExpires().getSeconds());
    }

    public Set<String> getUserAllSessionIds(UserEnum userEnum, long userId) {
        return redisComponent.getKeys(IcebartechConstants.USER_SESSION_GROUP_KEY, userEnum.name() + ":" + userId + ":*");
    }

    /**
     * 用户注销登录
     */
    public void logout(String sessionId) {
        redisComponent.del(IcebartechConstants.USER_SESSION_GROUP_KEY, sessionId);
    }



    /**
     * 登录标记
     * @param id 用户
     * @return sessionId
     */
    public String getLocalUser(Long id,long time){
        String sessionId = composeSessionId(UserEnum.app, id);
        LocalUser localUser = new LocalUser();
        localUser.setUserEnum(UserEnum.app);
        localUser.setSessionId(sessionId);
        localUser.setLevel(0);
        localUser.setUserId(id);
        localUser.setTime(time);
        login(sessionId, localUser,time);
        return sessionId;
    }
}
