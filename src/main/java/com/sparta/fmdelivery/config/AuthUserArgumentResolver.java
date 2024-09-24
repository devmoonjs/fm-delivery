package com.sparta.fmdelivery.config;

import com.sparta.fmdelivery.common.annotation.Auth;
import com.sparta.fmdelivery.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

        if (hasAuthAnnotation != isAuthUserType) {
            throw new IllegalArgumentException("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
        }
        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(
                                   @Nullable MethodParameter parameter,
                                   @Nullable ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest,
                                   @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Long userId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("email");
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        return new AuthUser(userId, email, userRole);
    }
}
