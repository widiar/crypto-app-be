package com.project.crypto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
public class AuthCheckDto {
    private Collection<? extends GrantedAuthority> role;
    private Boolean isValid;
}
