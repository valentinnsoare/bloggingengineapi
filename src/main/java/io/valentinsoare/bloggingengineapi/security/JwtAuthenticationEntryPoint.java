package io.valentinsoare.bloggingengineapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.valentinsoare.bloggingengineapi.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorResponse newError = ErrorResponse.builder()
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .message(authException.getMessage())
                .details(request.getRequestURI())
                .build();

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, objectMapper.writeValueAsString(newError));
    }
}
