package org.example.spring_jwt_learning.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Builder
public class UserEntity {

   private String name;
   private String password;
   private String email;
   private Set<String> role;




}
