package com.icebartech.core.constants;

/**
 * 用户类型枚举值
 *
 * @author haosheng.wenhs
 */
public enum UserEnum {
    weixin,//小程序用户
    app,//app端用户
    admin,//后台用户
    h5_master, //h5用户端1
    h5_slaver,//h5用户端2
    agent,//代理商
    no_login;//无需登录 用于标记接口可以匿名访问
}
