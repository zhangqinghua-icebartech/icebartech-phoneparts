package com.icebartech.core.utils.hibernate;

import com.icebartech.core.utils.StringKit;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

public interface Property<T, R> extends Function<T, R>, Serializable {

    default String getName() {
        try {
            Method declaredMethod = this.getClass().getDeclaredMethod("writeReplace");
            declaredMethod.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) declaredMethod.invoke(this);
            String method = serializedLambda.getImplMethodName();
            String attr;
            if (method.startsWith("get")) {
                attr = method.substring(3);
            } else {
                attr = method.substring(2);
            }
            return StringKit.toLowerCaseFirstOne(attr);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
