package io.valentinsoare.bloggingengineapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.valentinsoare.bloggingengineapi.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

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

        String exception = (String) request.getAttribute("exception");

        ErrorResponse newError = ErrorResponse.builder()
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .timestamp(Instant.now())
                .message(exception)
                .details(request.getRequestURI())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                String.format(
                        "{\"statusCode\": %d, \"message\": \"%s\", \"details\": \"%s\", \"timestamp\": \"%s\"}",
                        newError.getStatusCode(),
                        newError.getMessage(),
                        newError.getDetails(),
                        newError.getTimestamp()
                )
        );
    }
}
