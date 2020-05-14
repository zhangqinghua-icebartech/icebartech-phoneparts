package com.icebartech.core.swagger2;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.*;

/**
 * swagger2判断RequireLogin注解 在文档增加sessionId参数
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequireLoginOperationBuilderPlugin2 implements OperationBuilderPlugin {
    @Override
    public void apply(OperationContext context) {
        List<RequireLogin> requireLoginOptional = context.findAllAnnotations(RequireLogin.class);
        if (CollectionUtils.isEmpty(requireLoginOptional)) {
            return;
        }
        Set<UserEnum> userEnums = new HashSet<>();
        Set<UserEnum> ignores = new HashSet<>();
        for (RequireLogin requireLogin : requireLoginOptional) {
            userEnums.addAll(Arrays.asList(requireLogin.userEnum()));
            userEnums.addAll(Arrays.asList(requireLogin.value()));
            ignores.addAll(Arrays.asList(requireLogin.ignore()));
        }
        // 清除要忽略的身份
        userEnums.removeAll(ignores);
        if (CollectionUtils.isEmpty(userEnums)) {
            return;
        }
        if (userEnums.contains(UserEnum.no_login)) {
            return;
        }
        Parameter tokenPar = new ParameterBuilder().name("sessionId")
                .description("sessionId")
                .parameterType("header")
                .required(true)
                .modelRef(new ModelRef("string"))
                .build();
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(tokenPar);
        context.operationBuilder().parameters(parameters);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
