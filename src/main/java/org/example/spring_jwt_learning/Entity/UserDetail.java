package org.example.spring_jwt_learning.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetail implements UserDetails {
   private UserEntity ue ;
    public UserDetail(UserEntity ue)
    {
        this.ue=ue;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> ct = new ArrayList<>();

        ct.add(()->ue.getRoles().toString());
        return ct;

    }

    @Override
    public String getPassword() {
        return ue.getPassword();
    }

    @Override
    public String getUsername() {
        return ue.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
