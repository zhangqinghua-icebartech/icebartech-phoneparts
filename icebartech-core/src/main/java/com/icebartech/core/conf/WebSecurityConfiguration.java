package com.icebartech.core.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * The Class WebSecurityConfiguration that sets up the OAuth2 single sign on
 * configuration and the web security associated with it.
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Define the security that applies to the proxy
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .and().authorizeRequests()
                .antMatchers("/", "/**/*").permitAll()
                .anyRequest().permitAll()
                .and().csrf().disable();

    }

}
