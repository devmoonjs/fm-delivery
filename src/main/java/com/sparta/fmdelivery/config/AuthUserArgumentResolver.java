package com.sparta.fmdelivery.config;

import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 컨트롤러 메서드의 파라미터를 확인:
     * @Auth 어노테이션이 붙어 있고, 파라미터 타입이 AuthUser인지 확인합니다.
     * 만약 이 조건이 맞지 않으면 예외를 발생시킵니다.
     *
     * JWT 필터에서 저장된 사용자 정보를 가져옴:
     * HttpServletRequest에서 필터에 의해 저장된 사용자 정보(userId, email, userRole)를 가져옵니다.
     *
     * AuthUser 객체 생성:
     * 사용자 정보를 바탕으로 AuthUser 객체를 생성하고, 컨트롤러 메서드의 파라미터로 전달합니다. 이로 인해 컨트롤러 메서드는 별도의 처리 없이 사용자 정보를 받아 처리할 수 있습니다.
     * @param parameter
     * @return
     */
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
