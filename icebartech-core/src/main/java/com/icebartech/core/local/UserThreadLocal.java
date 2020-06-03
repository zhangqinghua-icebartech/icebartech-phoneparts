package com.icebartech.core.local;

import com.icebartech.core.constants.UserEnum;
import org.springframework.util.Assert;

/**
 * 统一用户对象threadlocal类，每个线程绑定一个登录用户，注意该类禁止被继承
 *
 * @author istrator
 */
public final class UserThreadLocal {

    private static ThreadLocal<LocalUser> localUserThreadLocal = new ThreadLocal<>();

    /**
     * 设置登录用户
     *
     * @param user
     */
    public static void setUserInfo(LocalUser user) {
        localUserThreadLocal.set(user);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static LocalUser getUserInfo() {
        return getUserInfo(false);
    }

    public static LocalUser getUserInfo(Boolean nullable) {
        LocalUser localUser = localUserThreadLocal.get();
        if (!nullable) {
            Assert.notNull(localUser, "获取当前登录用户信息失败");
        }
        return localUser;
    }

    /**
     * 移除掉登录用户id
     */
    public static void removeUserInfo() {
        localUserThreadLocal.remove();
    }

    /**
     * 获取当前登陆用户Id
     *
     * @return
     */
    public static Long getUserId() {
        return getUserId(false);
    }

    public static Long getUserId(Boolean returnNull) {
        LocalUser localUser = localUserThreadLocal.get();
        Long userId;
        if (returnNull) {
            userId = null == localUser ? null : localUser.getUserId();
        } else {
            Assert.notNull(localUser, "获取当前登录用户信息失败");
            userId = localUser.getUserId();
        }
        return userId;
    }

    public static UserEnum getUserType() {
        return getUserType(false);
    }

    public static UserEnum getUserType(Boolean returnNull) {
        LocalUser localUser = localUserThreadLocal.get();
        UserEnum userEnum;
        if (returnNull) {
            userEnum = null == localUser ? null : localUser.getUserEnum();
        } else {
            Assert.notNull(localUser, "获取当前登录用户信息失败");
            userEnum = localUser.getUserEnum();
        }
        return userEnum;
    }
}