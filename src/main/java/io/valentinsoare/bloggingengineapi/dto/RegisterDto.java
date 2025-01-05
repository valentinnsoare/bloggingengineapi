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
@Schema(name = "RegisterDto", description = "Data Transfer Object for Register", hidden = true)
public class RegisterDto {
    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotBlank(message = "Username is mandatory!")
    private String username;

    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Email must be a valid email address!")
    private String email;

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters!")
    @NotBlank(message = "Password is mandatory!")
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
