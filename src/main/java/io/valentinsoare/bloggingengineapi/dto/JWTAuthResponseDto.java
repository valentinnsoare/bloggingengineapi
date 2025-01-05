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
@Schema(name = "JWTAuthResponseDto", description = "Data Transfer Object for JWT Authentication Response", hidden = true)
public class JWTAuthResponseDto {
    @NotBlank(message = "Access token is required")
    private String accessToken;

    @NotBlank(message = "Token type is required")
    private String tokenType;

    @Override
    public String toString() {
        return "JWTAuthResponseDto: [" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ']';
    }
}
