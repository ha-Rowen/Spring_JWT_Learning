package org.example.spring_jwt_learning;

import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.JDBC.JDBC_Repository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class SpringJwtLearningApplicationTests {

    @Autowired
    private        JDBC_Repository     JR;
    private static UserEntity        user;
    private static RandomString        RS;



    @BeforeAll
    @DisplayName("User..OK")
   static void UserEntity()
    {
        RS =new RandomString(9);
        user=new UserEntity();
        user.setUsername(RS.RandomString());
        user.setPassword(RS.RandomString());
        user.setRole("user");

    }

    @Test
    @DisplayName("DB_ADD_TEST")
    void DB_add_TEST() {
        JR.add(user);
    }

}
