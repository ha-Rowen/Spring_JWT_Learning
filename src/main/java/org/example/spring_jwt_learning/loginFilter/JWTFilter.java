package org.example.spring_jwt_learning.loginFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* JWT 방식으로 로그인을 진행하면 JWT토큰으로 인증을하게 된다. 그리고 항상
*  JWT토큰을 조합하여 서버와 통신할 때, 사용하게 되며 잠깐 동안이지만 1번의 세션을 생성하게 되지만 금방 해제되는 방식이다.
*  이번 클래스는 JWT 토큰을 확인하여 사용자를 확인하고 인증하는 과정이다. */
public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

      String Authorization=  request.getHeader("Authorization");


    }
}
