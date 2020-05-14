package com.icebartech.core.enums;

public enum ChooseType {
    y("是"),
    n("否");
    public String text;

    ChooseType(String text) {
        this.text = text;
    }

    public static ChooseType is(boolean value) {
        return value ? y : n;
    }

}
