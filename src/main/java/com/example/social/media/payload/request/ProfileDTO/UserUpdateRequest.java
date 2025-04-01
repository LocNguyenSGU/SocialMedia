package com.example.social.media.payload.request.ProfileDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 3, max = 50, message = "UserName must be between 3 and 50 characters")
    @NotBlank(message = "UserName is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username can only contain letters, numbers, and underscores"
    )
    private String userName;


    @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
    @NotBlank(message = "Firstname is required")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Firstname must contain only letters"
    )
    private String firstName;


    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    @NotBlank(message = "Lastname is required")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Lastname must contain only letters"
    )
    @NotNull(message = "Lastname is required")
    private String lastName;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format"
    )
    @NotBlank(message = "Email is required")
    private String email;



    @NotBlank(message = "PhoneNumber is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits"
    )
    private String phoneNumber;

    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must contain at least one letter and one number"
    )
    private String password;
}

