package com.icebartech.core.vo;

import java.util.List;

public class MenuButtion implements AbstractButton {
    private String name;
    private List<AbstractType> sub_button;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AbstractType> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<AbstractType> sub_button) {
        this.sub_button = sub_button;
    }

}
