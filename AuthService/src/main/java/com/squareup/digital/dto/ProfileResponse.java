package com.squareup.digital.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
  ObjectId id;
  String email;
  String firstName;
  String lastName;
  String profile;
  String username;
  Long followers;
  Long following;
  Instant createdAt;
  Instant updatedAt;
  Boolean verified = false;
  Long posts;
}
