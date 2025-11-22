package org.example.spring_jwt_learning.loginFilter;

import io.jsonwebtoken.Jwt;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JWTutil {

    protected String         jwtId;  // 솔직히 이게 아직 왜 쓸모있는지 모르겠다. 일단 계속 진행해보자
    protected SecretKey       keys;  // 테스트 코드에서 객체를 상속받고 변수 접근을 하기 위해서 protected를 사용
    protected String      Encodeds;// 테스트 코드에서 객체를 상속받고 변수 접근을 하기 위해서 protected를 사용

   // @Value("${String.TEST.JWT.KEY}")
    protected String          SpringKey="vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb";

    public JWTutil ( )
    {
        this.keys = Jwts.SIG.HS256.key().build();;
       this.jwtId    = java.util.UUID.randomUUID().toString();
       this.Encodeds = Encoders.BASE64.encode(this.keys.getEncoded()); // 검증로직에서 활용하기 위함

       // secretKey = new SecretKeySpec(keys.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        //이렇게 직접 작업하지 않아도  .signWith(this.keys)를 하면 알아서 라이브러리가 Base64 및 암호화를 전부 진행해줘서 괜찮음.
        // 0.12.3 버전에는 hmacShaKeyFor를 지원하지 않아서 어쩔 수 없다.
    }

    public String getRole(String jwt)
    {
        try {
            return Jwts.parser().verifyWith(keys).build().parseSignedClaims(jwt) //말 그대로 파싱과정
                    .getPayload().get("Role", List.class ).toString();
        } catch (JwtException | IllegalArgumentException e)
        {
            throw new RuntimeException("JWTutil.class: getRole 메서드 JWT파싱 검증실패",e);
        }
    }


    public String getUsername(String jwt)
    {

       return jwtVerification (jwt,String.class,"UserName","JWTutil.class: getUsername 메서드 JWT파싱 검증실패");

    }

    private <T> T jwtVerification (String jwt,Class<T> ReturnType, String claimName,String errorMessage  )
    {
        try {
            return Jwts.parser().verifyWith(keys).build().parseSignedClaims(jwt) //말 그대로 파싱과정
                    .getPayload().get(claimName, ReturnType);
        } catch (JwtException | IllegalArgumentException e)
        {
            throw new RuntimeException(errorMessage,e);
        }
    }


   /*{
             "username": "김철수",
             "age": 30,
             "isAdmin": true,
             "roles": ["USER", "ADMIN"],
             "loginTime": 1234567890
          } 실제 JWT payload 값이 이렇게 될 수 있으며 key:value로 구성되어 있다.  그러기 때문에 get()을 했을 때 타입이 일정하지 않아서 제네릭을 사용해서 명시한다.  */

    public String createJwt(UserEntity user , Long expiredMs)
    {
        return Jwts
                .builder()
                .id(jwtId)
                .claim("UserName",user.getName())
                .claim("Role",user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(keys)
                .compact();
    }



}
