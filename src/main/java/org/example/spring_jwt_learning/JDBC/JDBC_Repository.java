package org.example.spring_jwt_learning.JDBC;


import org.example.spring_jwt_learning.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;


@Repository
public  class JDBC_Repository implements RepositoryInterface{
    @Autowired
   private JdbcTemplate JT;

    @Override
   public int add(UserEntity user)
   {
       String sql = "INSERT INTO JWT_user (username,role,PASSWORD) VALUES (?,?,?)";
       return JT.update(sql, user.getName(), user.getRole(), user.getPassword());

   }



}
