package com.icebartech.base.message.service;

import com.icebartech.base.message.enums.CodeTypeEnum;

/**
 * @author Created by liuao on 2019/7/2.
 * @desc
 */
public interface MailService {

    /**
     * 发送验证码
     * @param mail 邮箱
     * @param type 类型
     * @return
     */
    Boolean sendCode(String mail, String type);


    /**
     * 核实验证码
     * @param mail 手机号
     * @param type 类型
     * @param code 验证码
     * @return Boolean
     */
    Boolean verify(String mail, String type,String code);
}
