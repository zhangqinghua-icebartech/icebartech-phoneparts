package com.icebartech.core.local;


import com.alibaba.fastjson.JSON;
import com.icebartech.core.constants.UserEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class LocalUser implements Serializable {

    private static final long serialVersionUID = -7246742082171864538L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * session_id
     */
    private String sessionId;

    private UserEnum userEnum;

    private String roleKey;

    private Integer level;

    private long time = 7*24*60*60;

    private Long parentId;

    /**
     * 携带的额外数据
     */
    private Object extData;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
