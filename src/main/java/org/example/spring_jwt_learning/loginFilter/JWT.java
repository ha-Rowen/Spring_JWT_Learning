package org.example.spring_jwt_learning.loginFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JWT extends UsernamePasswordAuthenticationFilter
{

    /*기본에 있던 Springboot security에서 필터 체인이라는 부분이 있다.
    * 거기에 존재하는 UsernamePasswordAuthenticationFilter를 상속받아서 커스텀하는 방법이다.
    * JWT방식을 사용하기 위한 방법이다.  */

    private final AuthenticationManager authenticationManager;
    private final JWTutil               jwTutil              ;
    public JWT(AuthenticationManager authenticationManager,JWTutil jwTutil)
    {
        this.jwTutil=jwTutil;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken UPAT = new UsernamePasswordAuthenticationToken(username, password);
        // 사용자 인증정보를 담는 객체
        return authenticationManager.authenticate(UPAT);

        /* 여기가 핵심적인 부분이라고 말할 수 있다.
         * 로그인 요청이 들어오면 서블릿이 username, password를 받아서 처리해준다.
         * UsernamePasswordAuthenticationToken 에서는 인증되지않는 사용자를 위해서 인증 토큰을 생성하고
         * authenticationManager에게 실제 인증 처리를 위임한다.
         * authenticationManager는  UserDetailsService와 PasswordEncoder를 통해
         * 사용자 정보 조회 및 비밀번호 검증을 수행한다.
         */

    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
     /*로그인이 성공했을 때 JWT토큰 발급 진행 */
        super.successfulAuthentication(request, response, chain, authResult);

        Object principal = authResult.getPrincipal();

    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
