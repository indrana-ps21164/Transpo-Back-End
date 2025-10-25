package com.transpo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private String passwordConfirm;
    private String role;
}
