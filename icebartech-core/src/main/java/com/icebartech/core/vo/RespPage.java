package com.icebartech.core.vo;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "RespPage", description = "返回的json数据对象")
public class RespPage<E> extends RespJson<PageData<E>> {

}
