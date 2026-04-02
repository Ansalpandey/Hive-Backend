package com.squareup.digital.validators;

import com.squareup.digital.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AuthValidator {
    public void validateUser(UserModel user) {

        if (user.getUsername().isBlank() || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password and username cannot be empty");
        }

        if (user.getUsername().length() < 4 || user.getUsername().length() > 20) {
            throw new IllegalArgumentException("Username must be between 4 and 20 characters");
        }

        if (!Pattern.matches("^[a-zA-Z0-9]*$", user.getUsername())) {
            throw new IllegalArgumentException("Username must contain only letters and numbers");
        }

        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            throw new IllegalArgumentException("Password must be between 6 and 20 characters");
        }

        if (!Pattern.matches(
                "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$",
                user.getPassword())) {
            throw new IllegalArgumentException(
                    "Password must contain at least one capital letter, one number, and one special character."
            );
        }
    }
}
