package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginDto", description = "Data Transfer Object for Login", hidden = true)
public class LoginDto {
    @NotBlank(message = "Username or email cannot be blank")
    @Schema(description = "Username or email", example = "user")
    private String usernameOrEmail;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password", example = "password")
    private String password;

    @Override
    public String toString() {
        return "LoginDto: [" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                ", password='" + password + '\'' +
                ']';
    }
}
