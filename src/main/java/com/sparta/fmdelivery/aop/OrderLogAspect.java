package com.sparta.fmdelivery.aop;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;

@Slf4j
@Aspect
public class OrderLogAspect {
    private final HttpServletRequest servletRequest;

    public OrderLogAspect(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    @Pointcut("execution(* com.sparta.fmdelivery.domain.order.controller.OrderController.createOrder(..)) || " +
            "execution(* com.sparta.fmdelivery.domain.order.controller.OrderController.acceptOrder(..))")
    private void orderMethods() {}

    @Around("orderMethods()")
    public Object recordOrderLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // API 접근 시각
        LocalDateTime accessTime = LocalDateTime.now();
        log.info("API accessed at: {}", accessTime);

        Object result = joinPoint.proceed();    // 메서드 실행

        // 결과가 ApiResponse<OrderResponse>인지 확인
        if (result instanceof ApiResponse) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) result;
            Object data = apiResponse.getData();

            if (data instanceof OrderResponse) {
                OrderResponse orderResponse = (OrderResponse) data;
                Long orderId = orderResponse.getOrderId();
                Long shopId = orderResponse.getShopId();

                log.info("Order ID: {}, Shop ID: {}", orderId, shopId); // 주문 ID, 가게 ID Log
            }
        }

        return result;  // 메서드의 실제 결과 반환
    }
}
