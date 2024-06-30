package com.example.onlinemarket.common.interceptor;


import com.example.onlinemarket.domain.user.exception.UnauthorizedUserException;
import com.example.onlinemarket.domain.user.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("로그인 체크 인터셉터 실행");

        loginService.getLoginUserId()
            .orElseThrow(() -> new UnauthorizedUserException("로그인 후 이용 가능합니다."));

        return true;
    }
}
