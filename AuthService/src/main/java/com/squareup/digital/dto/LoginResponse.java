package com.squareup.digital.dto;

import com.squareup.digital.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
  private String token;
  private UserModel user;
}
