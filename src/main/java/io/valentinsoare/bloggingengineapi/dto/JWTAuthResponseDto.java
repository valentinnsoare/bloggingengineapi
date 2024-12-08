package io.valentinsoare.bloggingengineapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JWTAuthResponseDto {
    @NotBlank(message = "Access token is required")
    private String accessToken;

    @NotBlank(message = "Token type is required")
    private String tokenType;

    @Override
    public String toString() {
        return "JWTAuthResponseDto{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
