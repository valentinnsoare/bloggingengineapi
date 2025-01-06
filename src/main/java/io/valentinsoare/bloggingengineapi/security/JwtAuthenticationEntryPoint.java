package io.valentinsoare.bloggingengineapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.valentinsoare.bloggingengineapi.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String exception = (String) request.getAttribute("exception");

        if (exception != null) {
            ErrorResponse newError = ErrorResponse.builder()
                    .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                    .timestamp(Instant.now())
                    .message(exception)
                    .details(request.getRequestURI())
                    .build();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            String content = String.format(
                    "{\n    statusCode: %d,\n    message: %s,\n    details: %s,\n    timestamp: %s\n }",
                    newError.getStatusCode(),
                    newError.getMessage(),
                    newError.getDetails(),
                    newError.getTimestamp()
            );

            log.error("{ statusCode: {}, message: {} }", newError.getStatusCode(), newError.getMessage());
            response.getWriter().write(content);
        }
    }
}
