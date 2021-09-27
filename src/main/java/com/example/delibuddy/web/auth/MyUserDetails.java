package com.example.delibuddy.web.auth;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    @Builder.Default private boolean isEnabled = true;
    @Builder.Default private boolean isAccountNonExpired = true;
    @Builder.Default private boolean isAccountNonLocked = true;
    @Builder.Default private boolean isCredentialsNonExpired = true;
    private Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(String username, String password, boolean isEnabled, boolean isAccountNonExpired,
        boolean isAccountNonLocked, boolean isCredentialsNonExpired, Collection<? extends GrantedAuthority> authorities
    ) {
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.authorities = authorities;
    }
}
