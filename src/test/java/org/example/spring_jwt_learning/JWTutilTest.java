package org.example.spring_jwt_learning;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.loginFilter.JWTutil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class JWTutilTest {
    // 스프링 컨테이너가 생성한 JWTutil 인스턴스를 주입받습니다.

    private  JwtTestClass jwtUtil  = new JwtTestClass()    ;
    private SecretKey keys        = jwtUtil.GetKeys()      ;
    private String    Encodeds    = jwtUtil.GetEncodeds()  ;
    private String    jwtid       = jwtUtil.GetJwtId()     ;



    private UserEntity user = UserEntity.builder()
                                 .name("김철수")
                                    .role("User")
                                        .email("12345798@naver.com")
                                             .password("dfafa")
                                            .build();

    @Test
    @DisplayName("JWT키값 검증")
    void keyInitializationTest() {
        // 1. 키와 코드가 null이 아닌지 확인
        assertThat(keys)
                .isNotNull();

        assertThat(Encodeds)
                .isNotNull();

        assertThat(jwtid)
                .isNotNull();

        assertThat(jwtUtil)
                .isNotNull();
        // 데이터 출력 해보기
        System.out.println("Injected Key: " + keys);
        System.out.println("Injected Encoded: " + Encodeds);
        System.out.println("Injected JwtId: "+ jwtid);
        System.out.println("Injected jwtUtil: "+ jwtUtil);



    }

    @Test
    @DisplayName("JWT 생성 및 파싱 검증")
    void Parse()
    {

       String JWT= jwtUtil.createJwt(user,1200L);

      Jws<Claims> claims = Jwts.parser()
                .verifyWith(this.keys)
                .build()
                .parseSignedClaims(JWT);

        assertThat(claims.getPayload().getId()).isEqualTo(this.jwtid);
        assertThat(claims.getPayload().get("UserName")).isEqualTo("김철수");
        assertThat(claims.getPayload().get("Role")).isEqualTo("User");

    }
}


class JwtTestClass extends JWTutil {





    public SecretKey GetKeys()
   {
       return keys;
   }

   public String GetEncodeds ()
   {
       return Encodeds;
   }

   public String GetJwtId()
   {
       return jwtId;
   }

}