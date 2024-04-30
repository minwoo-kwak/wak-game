package com.wak.game.global.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JWTUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthUser.class) != null && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization").substring(7);
        return jwtUtils.getId(token);
    }
}
