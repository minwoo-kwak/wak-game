package com.wak.game.global.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTInterceptor implements HandlerInterceptor {

    private final JWTUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long user_id = jwtUtils.getId(token);
            if (user_id != null && !jwtUtils.isExpired(token)) {
                request.setAttribute("user_id", user_id);
                return true; // JWT가 유효하면 요청을 계속 진행
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        return false; // JWT가 유효하지 않으면 요청을 차단
    }
}
