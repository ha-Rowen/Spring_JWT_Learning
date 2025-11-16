package org.example.spring_jwt_learning.Service;

import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.JoinDTO.UserJoinDTO;

public interface JoinService {


    int UserJoin(UserJoinDTO userdto);
}
