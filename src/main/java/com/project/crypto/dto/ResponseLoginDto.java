package com.project.crypto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class ResponseLoginDto {
    private String session;
    private String role;
}
