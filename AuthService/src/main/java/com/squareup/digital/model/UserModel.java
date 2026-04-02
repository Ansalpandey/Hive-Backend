package com.squareup.digital.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Document(collection = "users")
@TypeAlias("user")
public class UserModel {
    @Id
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    @Indexed(unique = true)
    private String email;
    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String role;
    private String profile;
}
