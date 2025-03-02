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

    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Invalid email format"
    )
    private String email;


    @NotBlank(message = "PhoneNumber is required")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Phone number must be between 10 and 15 digits"
    )
    private String phoneNumber;
}

