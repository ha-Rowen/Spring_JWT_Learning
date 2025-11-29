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
    // OncePerRequestFilter 이건 요청 1번당 1회 실행한다.
    JWTutil jwt;
    public JWTFilter (JWTutil jwt)
    {
        this.jwt=jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        String Authorization = request.getHeader("Authorization");
        /* Http요청에서 헤더에 기록되어 있는 Atuthorization값을 가져온다.
         * [Requst Line]
         * [Header Fields]  <-- 데이터가 담기는 부위
         * [Body]
        */


        // 토큰헤더 검증 (값이 없거나, Bearer에서 값이 없으면 필터를 중지 시킨다.
        if(Authorization==null||!Authorization.startsWith("Bearer "))
        {
            filterChain.doFilter(request,response);
            return;
        }

        String token = Authorization.split(" ")[1];
        // authentication헤더 기준으로 공백이 포함되어 있다.
        // 이코드는 그 공백을 배열로 나누어서 저장하기 때문에
        // 실제 JWT토큰 값이 존재하는 1 Index에 존재하는 값을 따로 저장한다.



        // 예외 처리를 통하여 문제가 생길 때, 처리.
        try {
            if(jwt.isExpired(token)) { // 토큰의 만료기간이 유효한지 확인한다. 유효하지 않으면 true를 반환
                filterChain.doFilter(request,response); // 필터 중지
                return;
            }

            String        name = jwt.getUsername(token);
            List<String>  role = jwt.getRole    (token);



            UserEntity user = UserEntity.builder()
                    .name(name)
                    .roles(role)
                    .password("password")
                    .build();

            UserDetail UD = new UserDetail(user);//

            UsernamePasswordAuthenticationToken UPTK = new UsernamePasswordAuthenticationToken(UD, null, UD.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(UPTK);
            // 가장 핵심 코드

            /*
            * SecurityContextHolder를 스프링시큐리티 라고 칭한다면
            * 초반 JWT를 받으면 스프링시큐리티에서 JWT를 이해하지 못한다. 따라서
            * 스프링시큐리티가 JWT를 이해할 수 있게 AuthenticationToken으로 변환하여 SecurityContextHolder에
            * 넘겨준다.
            *
            * 이걸 통해서 접근제한 경로에 해당 사용자가 권한인 있는지 여부를 인식해 코드가 올바르게 동작한다.
            * */



        } catch (Exception e) {

            e.printStackTrace();
            // 에러 발생 시 그냥 진행 (인증 실패 처리)
        }

        filterChain.doFilter(request,response);
        // 올바르게 동작했다면 필터를 중지 시킨다.
    }
}
