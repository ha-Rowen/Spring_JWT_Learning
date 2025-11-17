package org.example.spring_jwt_learning.Entity;

import lombok.Builder;
import lombok.Getter;



@Getter
@Builder

public class UserEntity {

   private String name;
   private String password;
   private String email;
   private String role;


}
