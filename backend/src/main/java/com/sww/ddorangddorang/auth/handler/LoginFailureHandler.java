package com.sww.ddorangddorang.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * JWT 로그인 실패 시 처리하는 핸들러
 * SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 */
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        org.springframework.security.core.AuthenticationException exception)
        throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("로그인 실패! 이메일이나 비밀번호를 확인해주세요.");
        log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }
}