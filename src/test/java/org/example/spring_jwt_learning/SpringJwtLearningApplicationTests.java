package org.example.spring_jwt_learning;

import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.JDBC.JDBC_Repository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringJwtLearningApplicationTests {

    @Autowired
    private JDBC_Repository JR;
    private static UserEntity user;
    private static RandomString RS;

    @Autowired
    private BCryptPasswordEncoder bp;
    @Autowired
    private DataSource dataSource;

    @BeforeAll
    @DisplayName("User..OK")
    static void UserEntity() {
        RS = new RandomString(9);
        Set<String> Roles = new HashSet<>();
        Roles.add("user");
        Roles.add("admin");
        Roles.add("moderator");
        user = UserEntity.builder()
                .name(RS.RandomString())
                .email("hak.re@gmail.com")
                .password("ks18491ec3116")
                .roles(Roles)
                .build();

        System.out.println(user.getRoles());
    }


    @Test
    @DisplayName("DB연결 문제없음")
    @Order(1)
    void testDatabaseConnection() throws SQLException {
        // DB 연결 시도
        try (Connection connection = dataSource.getConnection()) {
            Assertions.assertNotNull(connection);
        }
    }

    @Test
    @DisplayName("DB_ADD_TEST")
    @Order(2)
    void DB_add_TEST() {
        user.setPassword(bp.encode("ks18491ec3116"));
        user.setName("명방이");
        JR.add(user);
    }

    @Test
    @DisplayName("사용자 password 인증")
    void authentication()
    {

        String password="ks18491ec3116";
        UserEntity usertest = UserEntity
                .builder()
                .password("$2a$12$qtr1fcH9u3RASD.Z4Oy2BuwfPJWukxbEis9t.QpDzx6xpfpSZG1Wq")
                .build();

           assertTrue(JR.userAuthentication(password,usertest));




    }

    @Test
    @DisplayName("다중사용자 권한 불러오기")
    @Order(3)
    void userJoin()
    {
        UserEntity user= JR.getUserEntity("jonghak.re@gmail.com");
       assertThat(user.getRoles()).containsExactly("user");
       System.out.println(user.getName());
       System.out.println(user.getPassword());


    }



}
