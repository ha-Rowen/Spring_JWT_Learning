package org.example.spring_jwt_learning.Service;

import org.example.spring_jwt_learning.Entity.UserDetail;
import org.example.spring_jwt_learning.Entity.UserEntity;
import org.example.spring_jwt_learning.JDBC.RepositoryInterface;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustumUserDetailService implements UserDetailsService {
        BCryptPasswordEncoder BP        ;
        RepositoryInterface   Repository;
        public CustumUserDetailService(RepositoryInterface Rt,BCryptPasswordEncoder BP)
        {
            this.Repository = Rt;
            this.BP = BP;
        }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                 /* 해당 메서드의 인자 값을 늘리는건 불가능하다고 한다.
                  * email 테이블은 중복이 불가능 조건에서 password는 security가 알아서 검증한다고 한다.
                  * 대신 user에 대한 정보를 제대로 줘야한다.
                  */
        UserEntity user = this.Repository.getUserEntity(email);

        if(user !=null)
            return new UserDetail(user);
        else
            return null;

    }
}
