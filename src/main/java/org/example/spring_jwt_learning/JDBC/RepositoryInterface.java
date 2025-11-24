package org.example.spring_jwt_learning.JDBC;

import org.example.spring_jwt_learning.Entity.UserEntity;

public interface RepositoryInterface {

     int add   (UserEntity user);
     int getRoleIdByName (String name);
     void insertUserRole(int userId, int roleId);
    int getUserIdJoin(String password);
     boolean userAuthentication (String password,UserEntity user);
     UserEntity getUserEntity (String email);

}
