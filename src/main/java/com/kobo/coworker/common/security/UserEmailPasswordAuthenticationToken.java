package com.kobo.coworker.common.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class UserEmailPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public UserEmailPasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
