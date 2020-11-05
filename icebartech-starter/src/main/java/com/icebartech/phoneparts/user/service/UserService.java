package com.icebartech.phoneparts.user.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.param.UserInsertParam;
import com.icebartech.phoneparts.user.po.LoginDto;
import com.icebartech.phoneparts.user.po.User;

/**
 * @author pc
 * @Date 2019-06-18T11:03:37.885
 * @Description 用户表
 */

public interface UserService extends BaseService
<UserDto, User> {

    /**
     * 注册
     * @param param
     * @return
     */
    Long register(UserInsertParam param);

    /**
     * 登录
     * @param email 邮箱
     * @param pwd 密码
     * @return
     */
    UserDto login(String email, String pwd);

    /**
     * 修改头像
     * @param id 用户id
     * @param headPortrait 头像
     * @return
     */
    Boolean changeHead(Long id,String headPortrait);

    /**
     * 忘记密码
     * @param email 邮箱
     * @return
     */
    Boolean changePwd(String email,String pwd);

    /**
     * 添加次数
     * @param userId 用户id
     * @param num 数量
     * @return
     */
    Boolean addUseCount(Long userId, Integer num);

    /**
     * 添加次数
     * @param code 兑换码
     * @return
     */
    Boolean addUseCount(String code);

    /**
     * 减少次数
     * @param userId 用户id
     * @return
     */
    Boolean reduceUseCount(Long productId,Long userId);

    /**
     *
     * @param userId 用户id
     * @param num 减少次数
     * @return
     */
    Boolean reduceUseCount(Long userId,Integer num);

    /**
     * 序列号登录
     * @param serialNum 序列号
     * @return
     */
    UserDto codeLogin(String serialNum);
}
