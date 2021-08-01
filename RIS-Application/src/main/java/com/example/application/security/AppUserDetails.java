package com.example.application.security;
import java.util.*;

import com.example.application.persistence.Role;
import com.example.application.persistence.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
 
public class AppUserDetails implements UserDetails {
 

    public Long getUserId(){
        return user.getUser_id();
    }
    /**
     *
     */
    private static final long serialVersionUID = 5470294977385388900L; // Dunno what this is yet
    private User user;
     
    public AppUserDetails(User user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
         
        return authorities;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.getEnabled();
    }
 
}
