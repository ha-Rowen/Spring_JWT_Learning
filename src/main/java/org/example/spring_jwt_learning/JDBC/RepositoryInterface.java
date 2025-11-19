package org.example.spring_jwt_learning.JDBC;

import org.example.spring_jwt_learning.Entity.UserEntity;

public interface RepositoryInterface {

    public int add (UserEntity user);
}
