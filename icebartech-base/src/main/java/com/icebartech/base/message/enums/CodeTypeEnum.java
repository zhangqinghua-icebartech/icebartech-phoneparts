package com.icebartech.base.message.enums;

/**
 * @author Created by liuao on 2019/5/17.
 * @desc
 */
public enum CodeTypeEnum {
    VERTIME(3600,"计数有效时间"),
    TIME(600,"有效时间"),
    REGISTER(600,"注册类型"),
    REPLACEPHONE(600,"更改手机号"),
    RESETPWD(600,"重置，密码"),
    RESETPAYPWD(600,"重置支付密码"),
    _NUM(10,"发送验证码计数类型");
    private long key;
    private String desc;

    CodeTypeEnum(long key, String desc){
        this.key = key;
        this.desc = desc;
    }

    public long getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
