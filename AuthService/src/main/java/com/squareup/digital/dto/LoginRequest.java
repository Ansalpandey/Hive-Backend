package com.squareup.digital.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
  private String username;
  private String password;
  private String email;
}
