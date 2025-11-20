package org.example.spring_jwt_learning;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import org.example.spring_jwt_learning.loginFilter.JWTutil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import java.beans.Encoder;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class JWTutilTest {
    // 스프링 컨테이너가 생성한 JWTutil 인스턴스를 주입받습니다.
    private JwtTestClass jwtUtil  = new JwtTestClass()     ;

    private SecretKey kets        = jwtUtil.GetKeys()      ;
    private String    Encodeds    = jwtUtil.GetEncodeds()  ;
    private String    jwtid       = jwtUtil.GetJwtId()     ;

    @Test
    void keyInitializationTest() {
        // 1. 키와 코드가 null이 아닌지 확인
        assertThat(kets)
                .isNotNull();

        assertThat(Encodeds)
                .isNotNull();

        assertThat(jwtid)
                .isNotNull();
        // 데이터 출력 해보기
        System.out.println("Injected Key: " + kets);
        System.out.println("Injected Encoded: " + Encodeds);
        System.out.println("Injected JwtId: "+ jwtid);

    }
}


class JwtTestClass extends JWTutil{

    public JwtTestClass(String keys) {
        super(keys);
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