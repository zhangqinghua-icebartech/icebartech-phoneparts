package com.icebartech.core.factory;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.security.auth.Destroyable;
import java.util.concurrent.TimeUnit;

/**
 * okhttp3，http后面建议都改成okhttp3
 *
 * @author wenhsh
 */
@Component
public class OkHttpClientFactory implements InitializingBean, FactoryBean<OkHttpClient>, Destroyable {

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build();

    @Override
    public OkHttpClient getObject() {
        return okHttpClient;
    }

    @Override
    public Class<?> getObjectType() {
        return OkHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public void destroy() {

    }

}
