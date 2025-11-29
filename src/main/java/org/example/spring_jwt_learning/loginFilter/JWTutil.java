package org.example.spring_jwt_learning.loginFilter;

import io.jsonwebtoken.*;


import io.jsonwebtoken.io.Encoders;
import org.example.spring_jwt_learning.Entity.UserDetail;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JWTutil {

    protected String         jwtId;  // 솔직히 이게 아직 왜 쓸모있는지 모르겠다. 일단 계속 진행해보자
    protected SecretKey       keys;  // 테스트 코드에서 객체를 상속받고 변수 접근을 하기 위해서 protected를 사용
    protected String      Encodeds;  // 테스트 코드에서 객체를 상속받고 변수 접근을 하기 위해서 protected를 사용
    // 테스트 코드에서 이 변수를 참조하기 위해서 JWTutil 객체를 상속받는 전용 객체를 만들었다...
    // 하지만 아직도 접근자 제한을 protected으로 사용하는건 올바른지 잘모르겠다.. AI와 토론해도 쉽게 결정하지 못하겠다...
    // 일단 사용해보면서 문제가 생기면 그때 교훈을 얻겠지...


    protected String key;
    //비밀키 받아오는 변수이다.
    // 마음 같아서는 생성자에 넣어서


    public JWTutil (@Value("${String.TEST.JWT.KEY}")String Key)
    {


        this.keys    = new SecretKeySpec(Key.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
       this.jwtId    = java.util.UUID.randomUUID().toString();
       this.Encodeds = Encoders.BASE64.encode(this.keys.getEncoded()); // 검증로직에서 활용하기 위함

       // secretKey = new SecretKeySpec(keys.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        //이렇게 직접 작업하지 않아도  .signWith(this.keys)를 하면 알아서 라이브러리가 Base64 및 암호화를 전부 진행해줘서 괜찮음.
        // 0.12.3 버전에는 hmacShaKeyFor를 지원하지 않아서 어쩔 수 없다.
    }


    public Boolean isExpired(String jwt)
    {
        return jwtVerification(jwt, Boolean.class,"Exp","JWTutil.class: isExpired 메서드 JWT파싱 검증실패");
    }


    public List<String> getRole(String jwt)
    {
       return jwtVerification (jwt,List.class,"Role","JWTutil.class: getRole 메서드 JWT파싱 검증실패");
    }


    public String getUsername(String jwt)
    {
       return jwtVerification (jwt,String.class,"UserName","JWTutil.class: getUsername 메서드 JWT파싱 검증실패");
    }

    private <T> T jwtVerification (String jwt,Class<T> ReturnType, String claimName,String errorMessage  )
    {   /* 처음에 예외처리를 하는 로직을 보고 이런 생각을 했다.
         1. 예외처리 자체가 컴퓨터의 자원을 많이 소모해서 많이 사용하는건 안좋다고...
         2. 검증로직마다 예외처리를 계속해줘야 한다고?? 뭔...
         과정: AI와 토론을 하고 블로그 예시 코드확인 결과 쓰는게 좋아보이네 ^^;
         결과: 예외처리는 그렇다 하지만 계속 중복된 코드를 작성하는게 부담스럽고, 유지보수에도 좋아보이지 않아서
              최대한 코드 재활용을 할 수 있게 검증 전용 메서드를 작성했다.

         교훈: 사용해보니까 정말 나쁘지 않다. 앞으로 비슷한게 있으면 적극적으로 만들자*/

        try {


                if(ReturnType== Boolean.class && claimName.equals("Exp")) {

                   return ReturnType.cast(
                           Jwts
                                   .parser()
                                   .verifyWith(keys)
                                   .build()
                                   .parseSignedClaims(jwt)
                                   .getPayload()
                                   .getExpiration()
                                   .before(new Date()));
                }
                else {
                    return
                            Jwts
                                    .parser()
                                    .verifyWith(keys)
                                    .build()
                                    .parseSignedClaims(jwt) //말 그대로 파싱과정
                                    .getPayload()
                                    .get(claimName, ReturnType);

                }
        }
        catch (ExpiredJwtException e) {
            // 만료된 경우 true 반환
            return ReturnType.cast(true);
        }
        catch (JwtException | IllegalArgumentException e)
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

    public String createJwt(String username ,List<String> roles, Long expiredMs)
    {
        return Jwts
                .builder()
                .id(jwtId)
                .claim("UserName",username)
                .claim("Role",roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(keys)
                .compact();
    }



}
