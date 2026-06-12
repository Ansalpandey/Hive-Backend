package com.squareup.digital.model;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
@TypeAlias("user")
public class UserModel {
  @Id private String id;

  @Indexed(unique = true)
  @NotBlank
  private String username;

  @NotBlank private String password;

  @NotBlank
  @Indexed(unique = true)
  private String email;

  @NotBlank private String firstName;
  private String lastName;
  private String profile;
  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();
  private Boolean verified = false;
  private Long followers = 0L;
  private Long following = 0L;
  private Long posts = 0L;
}
