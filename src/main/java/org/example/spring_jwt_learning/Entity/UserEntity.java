package org.example.spring_jwt_learning.Entity;
import lombok.Data;
import lombok.Singular;
import lombok.Builder;
import lombok.Getter;


import java.util.Set;


@Getter
@Builder
@Data
public class UserEntity {

   private String name;
   private String password;
   private String email;

   @Singular
   private Set<String> roles;




}
