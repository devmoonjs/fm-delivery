package com.sparta.fmdelivery.aop;

import com.sparta.fmdelivery.domain.order.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;

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

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 결과가 ResponseEntity<OrderResponse> 인지 확인
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();

            if (body instanceof OrderResponse) {
                OrderResponse orderResponse = (OrderResponse) body;
                Long orderId = orderResponse.getOrderId();
                Long shopId = orderResponse.getShopId();

                // 로그로 주문 ID와 가게 ID 기록
                log.info("Order ID: {}, Shop ID: {}", orderId, shopId);
            }
        }

        return result;  // 메서드의 실제 결과 반환
    }
}
