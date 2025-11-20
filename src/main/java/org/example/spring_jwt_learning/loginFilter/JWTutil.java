package org.example.spring_jwt_learning.loginFilter;

import io.jsonwebtoken.Jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JWTutil {
    SecretKey key;

    public JWTutil (@Value("${String.TEST.JWT.KEY}")String keys )
    {

    }


}
