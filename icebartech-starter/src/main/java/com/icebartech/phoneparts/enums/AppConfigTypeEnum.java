package com.icebartech.phoneparts.enums;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
public enum  AppConfigTypeEnum {

    IOS("IOS"),
    ANDROID("安卓");

    public String desc;

    AppConfigTypeEnum(String desc){
        this.desc = desc;
    }

}
