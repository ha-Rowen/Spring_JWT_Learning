package org.example.spring_jwt_learning.loginFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.spring_jwt_learning.Entity.UserDetail;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     /*로그인이 성공했을 때 JWT토큰 발급 진행
     * request    :Http from에서 입력한 데이터, 헤더, 쿠키를 가져옴
     * response   :서버가 클라이언트에 응답을 보낼 때 사용
     * chain      :현재 요청을 다음 필터로 넘길 때
     * authResult :로그인한 사용자 정보가 담겨 있음
     * */
        UserDetail user = (UserDetail) authResult.getPrincipal();
     /*   jwTutil.createJwt(user,60*60*10L);.claim("Role",user.getAuthorities())
         이렇게 객체를 직접 넣어서 .claim("Role",user.getAuthorities())를 하면 json 직렬화 문제가 생길 수 있다.*/

        List<String> roles = new ArrayList<>();
        for(GrantedAuthority tem:user.getAuthorities())
        {
            roles.add(tem.getAuthority());
        }
        String username = user.getUsername();

        String token = jwTutil.createJwt(username,roles,60*60*10L);
        response.addHeader("Authorization", "Bearer " + token);


    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
