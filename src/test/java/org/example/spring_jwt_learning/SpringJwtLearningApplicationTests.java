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
                .email("test20@gmail.com")
                .password("744sss5789s1")
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
        JR.add(user);
    }

    @Test
    @DisplayName("사용자 password 인증")
    void authentication()
    {
        String password="744sss5789s1";
        UserEntity usertest = UserEntity
                .builder()
                .password("$2a$12$ItlkoJjaHjEF3Nz03aXaWOPcChJhuqbRYy0OXkNqjEyPPi6qskfBO")
                .build();


       assertTrue(JR.userAuthentication(password,usertest));
    }

    @Test
    @DisplayName("다중사용자 권한 불러오기")
    @Order(3)
    void userJoin()
    {
        UserEntity user= JR.getUserEntity(this.user.getEmail(),this.user.getPassword());
       assertThat(user.getRoles()).containsExactly("user","admin","moderator");
    }



}
