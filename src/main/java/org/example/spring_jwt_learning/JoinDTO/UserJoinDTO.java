package org.example.spring_jwt_learning.JoinDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserJoinDTO {

    private String username;
    private String password;
    private String email;


}
