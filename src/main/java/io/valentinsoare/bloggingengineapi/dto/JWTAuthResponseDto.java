package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "Data Transfer Object for JWT Authentication Response")
public class JWTAuthResponseDto {
    @NotBlank(message = "Access token is required")
    @Schema(description = "Access token",
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjI5MjIwNjI4LCJleHAiOjE2Mjk4MjU0Mjh9.1",
            format = "JWT"
    )
    private String accessToken;

    @NotBlank(message = "Token type is required")
    @Schema(description = "Token type", example = "Bearer")
    private String tokenType;

    @Override
    public String toString() {
        return "JWTAuthResponseDto: [" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ']';
    }
}
