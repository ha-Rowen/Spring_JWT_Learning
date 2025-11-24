package org.example.spring_jwt_learning.JDBC;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class JDBC_Repository implements RepositoryInterface{
    @Autowired
    private JdbcTemplate JT;
    @Autowired
    private BCryptPasswordEncoder bp;
/*SELECT
    u.id,           -- 일반 컬럼
    u.name,         -- 일반 컬럼
    u.email,        -- 일반 컬럼
    GROUP_CONCAT(r.name) as roles  -- 집계 함수
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.id = 1
GROUP BY u.id, u.name, u.email;  */
    @Override
    public int add(UserEntity user) {
        int users;
        int userId;
        String password = bp.encode(user.getPassword());
        java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
        String sql = "INSERT INTO `users` (name,email,password,created_at,updated_at) VALUES (?,?,?,?,?)";
        users=JT.update(sql, user.getName(), user.getEmail(), password, now, now) ;

       userId=getUserIdJoin(password);

        user.getRoles().stream()
                .forEach(roleName -> {
                    int roleId = getRoleIdByName(roleName);  // role 이름으로 ID 조회
                    insertUserRole(userId, roleId);          // user_roles에 삽입
                });

      /*  user.getRoles().stream()
              .map(this::getRoleIdByName)
                    .forEach(roleId -> insertUserRole(userId, roleId));*/
        /* 내가 스트림 잘못하는거 같네.. 좀 더 공부하거나 연습하자 */
        if(users==1 && userId==1)
        {
            return 1;
        }else {
            return 0;
        }

    }

    public int getRoleIdByName (String name)
    {
        String sql="SELECT id FROM `roles` WHERE name =?";
        return JT.queryForObject(sql, Integer.class, name);
    }

    public void insertUserRole(int userId, int roleId)
    // 원리 try 붙이면서 잘 만들어야 하는데 공부용 예제에 너무 힘쓰는거 같아서 생략...
    {
        String sql = "INSERT INTO user_roles (user_id, role_id, created_at) VALUES (?,?,?)";
        java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
        JT.update(sql, userId, roleId, now) ;
    }


    public int getUserIdJoin(String password)
    {
       String sql="SELECT id FROM `users` WHERE password =?";
        return JT.queryForObject(sql, Integer.class, password);
    }


    public UserEntity getUserEntity (UserEntity user)
    {
        String sql = "SELECT u.id, u.name, u.email, GROUP_CONCAT(r.name) as roles " +
                "FROM users u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                "LEFT JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.password = ? " +
                "GROUP BY u.id, u.name, u.email";

        try {
            return JT.queryForObject(sql, (rs, rowNum) -> {
                // RowMapper를 사용해서 ResultSet을 UserEntity로 변환
                UserEntity.UserEntityBuilder builder = UserEntity.builder()
                        .name(rs.getString("name"))
                        .email(rs.getString("email"));

                // roles 처리 (GROUP_CONCAT 결과를 Set으로 변환)
                String rolesString = rs.getString("roles");
                if (rolesString != null && !rolesString.isEmpty()) {
                    String[] rolesArray = rolesString.split(",");
                    for (String role : rolesArray) {
                        builder.role(role.trim()); // @Singular 사용
                    }
                }

                return builder.build();
            }, user.getPassword());

        } catch (EmptyResultDataAccessException e) {
            // 데이터가 없을 때 (JOIN 실패 포함)
            System.out.println("User not found with given password");
            return null; // 또는 throw new UserNotFoundException();
        } catch (DataAccessException e) {
            // 기타 데이터베이스 오류
            System.out.println("Database error while fetching user");
            throw new RuntimeException("Failed to fetch user", e);
        }
    }


    @Override
    public int update(UserEntity user) {
        return 0;
    }
}
