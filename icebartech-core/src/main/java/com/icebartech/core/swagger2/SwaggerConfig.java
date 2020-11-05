package com.icebartech.core.swagger2;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.icebartech.core.properties.SiteProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Autowired
    private SiteProperties siteProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(System.getProperty("os.name").startsWith("Mac") ? null : siteProperties.getHost())
                .apiInfo(new ApiInfoBuilder().title(siteProperties.getExplanation() + " API 接口文档")
                        .version(siteProperties.getVersion().toString())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.icebartech"))
                .paths(PathSelectors.any())
                .build();
    }
}