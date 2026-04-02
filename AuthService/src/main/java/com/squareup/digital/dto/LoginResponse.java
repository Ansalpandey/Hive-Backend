package com.squareup.digital.dto;

import com.squareup.digital.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private UserModel user;
}
