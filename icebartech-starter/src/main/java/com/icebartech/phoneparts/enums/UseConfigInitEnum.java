package com.icebartech.phoneparts.enums;

/**
 * @author Created by liuao on 2019/10/15.
 * @desc
 */
public enum  UseConfigInitEnum {

    NAME_A(1,"默认1"),
    SPEED_A(1,"速度1"),
    PRESSURE_A(75,"压力1"),

    NAME_B(1,"默认2"),
    SPEED_B(2,"速度2"),
    PRESSURE_B(45,"压力2");


    private int key;
    private String desc;

    UseConfigInitEnum(int key,String desc){
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

}
