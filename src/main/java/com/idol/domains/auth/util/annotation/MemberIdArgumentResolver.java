package com.idol.domains.auth.util.annotation;

import com.idol.domains.auth.domain.UserIdentity;
import com.idol.global.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.idol.global.exception.ExceptionMessage.AUTHENTICATION_MISSING;
import static com.idol.global.exception.ExceptionMessage.INVALID_PRINCIPAL_TYPE;

@Component
public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberId.class)
                && (parameter.getParameterType().equals(Long.class)
                || parameter.getParameterType().equals(String.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationException(AUTHENTICATION_MISSING);
        }

        if (authentication.getPrincipal() instanceof UserIdentity principal) {
            Class<?> parameterType = parameter.getParameterType();

            if (parameterType.equals(Long.class)) {
                return principal.memberId();
            }

            if (parameterType.equals(String.class)) {
                return principal.getMemberIdAsString();
            }
        }
        return null; // 인증 객체가 이상하면 null 반환
//        throw new AuthenticationException(INVALID_PRINCIPAL_TYPE);
    }
}