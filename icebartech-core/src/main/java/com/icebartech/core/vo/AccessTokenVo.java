package com.icebartech.core.vo;

import java.io.Serializable;

/**
 * 存放accessToken与expireTime过期时间
 *
 * @author Administrator
 */
public class AccessTokenVo implements Serializable {
    private static final long serialVersionUID = -8961353777753660885L;
    private String accessToken;
    private String accessExpires;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessExpires() {
        return accessExpires;
    }

    public void setAccessExpires(String accessExpires) {
        this.accessExpires = accessExpires;
    }
}
