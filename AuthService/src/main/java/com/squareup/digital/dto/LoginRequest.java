package com.squareup.digital.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
