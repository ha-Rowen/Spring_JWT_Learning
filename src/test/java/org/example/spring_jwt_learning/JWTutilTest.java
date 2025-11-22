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

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class JWTutilTest {
    // 스프링 컨테이너가 생성한 JWTutil 인스턴스를 주입받습니다.

    private  JwtTestClass jwtUtil  = new JwtTestClass("vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb"); //실험하다가 @mock 이걸 삭제를 못해서 삽질을 많이했다.. 주의하자..
    // 생성자로 비밀키를 받는거 같지만 사실 @Value 때문에.. 테스트코드는 Spring과 같이 구동되는게 아니라 어쩔 수 없었다... 마음에 안들지만
    // 이걸 해결하려고 다른걸 시도하면 더 안좋은 예시로 넘어가는거 같아서 최대한 타협하고 결정했다.
    private SecretKey keys        = jwtUtil.GetKeys()      ;
    private String    Encodeds    = jwtUtil.GetEncodeds()  ;
    private String    jwtid       = jwtUtil.GetJwtId()     ;

    Set<String> role = new HashSet<>();

    public Set<String> setRole() {
        role.add("User");
        role.add("Admin");
        return role;
    }

    private UserEntity user = UserEntity.builder()
                                 .name("김철수")
                                    .role(setRole()) //role이걸 2번 사용하면 불변으로 값으로 입력되서. 가급적이면 한번에 모아서 넣어라
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
        assertThat(claims.getPayload().get("Role").toString()).isEqualTo(this.role.toString());
        // getPayload().get() <- 이부분에 String.class 안해도 assertThat가 object 타입으로 비교해서 좋았는데.
        // Role부분에서 중첩 권한을 만들어 보니까 어려 문제가 있었다. 그래서 toString으로 할 수 밖에....
        // get("Role",String.class)이렇게 해도 상관없는데, isEqualTo와 보기좋게 통일하고 싶어서 작성했다.

    }


    @Test
    @DisplayName("검증로직 TEST")
    void Verification ()
    {
        // String는 불변객체이다. 따라서 버퍼를 사용해서 메모리 효율을 올리고 싶었지만...
        // 쓰읍... 이게 맞나?? 싶긴하다..
        // 버퍼를 사용하면 가독성이 안좋아지고, 가독성을 포기할 만큼 큰 차이는 없는거 같고....
        // 너무 고민되는 부분이다.
        String JWT  = jwtUtil.createJwt   (user,1200L);
        String Role = jwtUtil.getRole    (JWT)                 ;
        String name = jwtUtil.getUsername(JWT)                 ;
       assertThat(name).isEqualTo(this.user.getName() )        ;
       assertThat(Role).isEqualTo(this.role.toString())        ;
    }

}


class JwtTestClass extends JWTutil {


    public JwtTestClass(String Key) {
        super(Key);
    }

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