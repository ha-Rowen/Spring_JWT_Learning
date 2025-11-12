package org.example.spring_jwt_learning.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

    private int    id      ;
    private String username;
    private String password;
    private String role    ;

}
