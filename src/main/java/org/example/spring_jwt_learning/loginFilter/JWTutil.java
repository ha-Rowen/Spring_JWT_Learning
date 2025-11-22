package org.example.spring_jwt_learning.loginFilter;

import io.jsonwebtoken.Jwt;


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



    public String getUsername(String token)
    {
        return Jwts.parser().verifyWith(keys).build().parseSignedClaims(token) //말 그대로 파싱과정
                .getPayload().get("username", String.class);
       // 예외처리를 하는게 좋다고 한다. 예외처리는 자원을 많이 소비하는 걸로 알고 있는데 여기서는 뭐.. 쓰는게 좋아 보이는데...
    }


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
