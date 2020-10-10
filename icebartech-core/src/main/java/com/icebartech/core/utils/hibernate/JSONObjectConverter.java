package com.icebartech.core.utils.hibernate;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Anler
 * @Date 2019/1/3 15:31
 * @Description JSON字符串和fastjson.JSONObject互转
 */
@Converter(autoApply = true)
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {

    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        return jsonObject.toJSONString();
    }

    @Override
    public JSONObject convertToEntityAttribute(String dbData) {
        return JSONObject.parseObject(dbData);
    }

}
