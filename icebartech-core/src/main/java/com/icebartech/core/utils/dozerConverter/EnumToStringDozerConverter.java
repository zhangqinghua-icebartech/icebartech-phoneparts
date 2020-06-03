package com.icebartech.core.utils.dozerConverter;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Anler
 * @Date 2019/2/16 21:20
 * @Description
 */
public class EnumToStringDozerConverter extends DozerConverter<Enum, String> {

    private Class<Enum> prototypeA;
    private Class<String> prototypeB;

    public EnumToStringDozerConverter(Class<Enum> prototypeA, Class<String> prototypeB) {
        super(prototypeA, prototypeB);
        this.prototypeA = prototypeA;
        this.prototypeB = prototypeB;
    }

    @Override
    public String convertTo(Enum anEnum, String s) {
        if (null == anEnum) {
            return null;
        }
        return anEnum.name();
    }

    @Override
    public Enum convertFrom(String s, Enum anEnum) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        Object enumeration = null;
        Method[] ms = prototypeA.getMethods();
        for (Method m : ms) {
            if (m.getName().equalsIgnoreCase("valueOf")) {
                try {
                    enumeration = m.invoke(prototypeA, s);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return (Enum) enumeration;
            }
        }
        return null;
    }
}
