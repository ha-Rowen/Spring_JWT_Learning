package org.example.spring_jwt_learning.loginFilter;

import io.jsonwebtoken.Jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTutil {

    protected String    jwtId;
    protected SecretKey  keys;  // 테스트 코드에서 객체를 상속받고 변수 접근을 하기 위해서 protected를 사용
    protected String Encodeds; // 테스트 코드에서 객체를 상속받고 변수 접근을 하기 위해서 protected를 사용
    protected SecretKey  secretKey;
    public JWTutil (@Value("${String.TEST.JWT.KEY}")String keys )
    {
       this.keys     = Jwts.SIG.HS256.key().build();
       this.jwtId    = java.util.UUID.randomUUID().toString();
       /// 이거 createJwt 메서드로 가야하나???
        // 아직 JWTutil의 활동 주기를 모르니까 확인하고 id는 매번 새롭게 필요한건지 확인하고 메서드에 넣기로 하자
       this.Encodeds = Encoders.BASE64.encode(this.keys.getEncoded());

        secretKey = new SecretKeySpec(keys.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());// test용이다
    }


    public String getUsername(String token)
    {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
       // 예외처리를 하는게 좋다고 한다. 예외처리는 자원을 많이 소비하는 걸로 알고 있는데 여기서는 뭐.. 쓰는게 좋아 보이는데...
    }


    public String createJwt(String username, String role, Long expiredMs)
    {
        return Jwts
                .builder()
                .id(jwtId)
                .claim("username",username)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(this.keys)
                .compact();
    }



}
