package org.example.spring_jwt_learning.Service;

import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.JDBC.RepositoryInterface;
import org.example.spring_jwt_learning.JoinDTO.UserJoinDTO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JdbcJoinService implements JoinService{

    private final BCryptPasswordEncoder BCrypt;

    private final RepositoryInterface JdbcRepository;

    public JdbcJoinService(RepositoryInterface JdbcRepository, BCryptPasswordEncoder BCrypt) {
        this.JdbcRepository = JdbcRepository;
        this.BCrypt = BCrypt;
    }



    @Override
    public int UserJoin(UserJoinDTO userdto) {
        UserEntity userEntity =  UserEntity.builder()
                .name(userdto.getUsername())
                .password( BCrypt.encode( userdto.getUsername()))
                .role("User").build();


        return  JdbcRepository.add(userEntity);


    }
}
