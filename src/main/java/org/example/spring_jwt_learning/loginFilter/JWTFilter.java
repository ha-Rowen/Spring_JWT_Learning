package org.example.spring_jwt_learning.loginFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.spring_jwt_learning.Entity.UserDetail;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.Service.CustumUserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/* JWT 방식으로 로그인을 진행하면 JWT토큰으로 인증을하게 된다. 그리고 항상
*  JWT토큰을 조합하여 서버와 통신할 때, 사용하게 되며 잠깐 동안이지만 1번의 세션을 생성하게 되지만 금방 해제되는 방식이다.
*  이번 클래스는 JWT 토큰을 확인하여 사용자를 확인하고 인증하는 과정이다. */
public class JWTFilter extends OncePerRequestFilter {
    JWTutil jwt;
    public JWTFilter (JWTutil jwt)
    {
        this.jwt=jwt;
    }

    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("필터시작");

      String Authorization = request.getHeader("Authorization");

      // 토큰헤더 검증
        if(Authorization==null||!Authorization.startsWith("Bearer "))
        {
            System.out.println("token null");
            System.out.println("토큰값 확인"+Authorization);
            filterChain.doFilter(request,response);

            // 조건이 되면 종료
            return;
        }

        String token = Authorization.split(" ")[1];
        System.out.println("토큰"+token); // 토큰확인

       if(jwt.isExpired(token))// 토큰 유효시간 확인
       {
           filterChain.doFilter(request,response);
           return;
       }

       String name = jwt.getUsername(token);
       String role = jwt.getRole(token);

       UserEntity user = UserEntity.builder().name(name).role(role).password("password").build();
       UserDetail UD = new UserDetail(user);
        UsernamePasswordAuthenticationToken UPTK = new UsernamePasswordAuthenticationToken(UD,null,UD.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(UPTK);
        filterChain.doFilter(request,response);




    }*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("필터시작");

        String Authorization = request.getHeader("Authorization");

        // 토큰헤더 검증
        if(Authorization==null||!Authorization.startsWith("Bearer "))
        {
            System.out.println("token null");
            System.out.println("토큰값 확인"+Authorization);
            filterChain.doFilter(request,response);
            return;
        }

        String token = Authorization.split(" ")[1];
        System.out.println("토큰: " + token);

        // 예외 처리 추가!
        try {
            if(jwt.isExpired(token)) {
                System.out.println("토큰 만료됨");
                filterChain.doFilter(request,response);
                return;
            }

            String name = jwt.getUsername(token);
            List<String> role  = jwt.getRole(token);

            System.out.println("name: " + name + ", role: " + role);

            UserEntity user = UserEntity.builder()
                    .name(name)
                    .roles(role)
                    .password("password")
                    .build();

            UserDetail UD = new UserDetail(user);
            UsernamePasswordAuthenticationToken UPTK =
                    new UsernamePasswordAuthenticationToken(UD, null, UD.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(UPTK);

        } catch (Exception e) {
            System.out.println("JWT 파싱 에러: " + e.getMessage());
            e.printStackTrace();
            // 에러 발생 시 그냥 진행 (인증 실패 처리)
        }

        filterChain.doFilter(request,response);
    }
}
