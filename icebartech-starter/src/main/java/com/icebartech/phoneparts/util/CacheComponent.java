package com.icebartech.phoneparts.util;

import com.icebartech.core.components.RedisComponent;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheComponent {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisComponent redisComponent;

    private final static String user_detail = "query_cache:user_detail";

    public UserDto getUserDetail(Long userId) {
        try {
            // 1. 查询用户信息
            Object o = redisComponent.get(user_detail, String.valueOf(userId));
            if (o == null) return null;

            // 2. 重置缓存时间
            redisComponent.expire(user_detail, String.valueOf(userId), 30 * 60);

            return (UserDto) o;
        } catch (Exception e) {
            log.info("用户详情获取Redis数据异常：" + e.getMessage(), e);
            delUserDetail(userId);
        }
        return null;
    }

    /**
     * 缓存用户详情半个小时
     * 1. update
     * 1. delete
     *
     * @param userId 用户Id
     */
    public void setUserDetail(Long userId, UserDto user) {
        try {
            redisComponent.set(user_detail, String.valueOf(userId), user, 30 * 60);
        } catch (Exception e) {
            log.info("用户详情存储Redis数据异常：" + e.getMessage(), e);
            delUserDetail(userId);
        }
    }

    /**
     * 删除用户详情
     *
     * @param userId 用户Id
     */
    public void delUserDetail(Long userId) {
        redisComponent.del(user_detail, String.valueOf(userId));
    }
}