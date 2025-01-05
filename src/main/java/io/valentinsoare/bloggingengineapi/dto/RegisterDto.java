package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Register")
public class RegisterDto {
    @NotBlank(message = "Name is mandatory!")
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @NotBlank(message = "Username is mandatory!")
    @Schema(description = "Username of the user", example = "user")
    private String username;

    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Email must be a valid email address!")
    @Schema(description = "Email of the user", example = "unknown@gmail.com")
    private String email;

    @NotBlank(message = "Password is mandatory!")
    @Schema(description = "Password of the user", example = "password")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters!")
    private String password;

    private Set<String> roles = new HashSet<>();

    @Override
    public String toString() {
        return "RegisterDto [" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ']';
    }
}
