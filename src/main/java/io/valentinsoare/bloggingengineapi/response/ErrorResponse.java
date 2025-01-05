package io.valentinsoare.bloggingengineapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "Error Response", hidden = true)
public class ErrorResponse {
    @NotNull
    @Schema(description = "Timestamp of the error", example = "2021-08-18T12:51:36.000Z")
    private Instant timestamp;

    @Size(max = 500)
    @NotBlank(message = "Message is mandatory!")
    @Schema(description = "Message of the error", example = "Internal Server Error")
    private String message;

    @Size(max = 255)
    @NotBlank(message = "Details is mandatory!")
    @Schema(description = "Details of the error", example = "An error occurred while processing the request")
    private String details;

    @Schema(description = "Status code of the error", example = "500")
    @Min(value = 100, message = "Status code must be greater than or equal to 100!")
    private int statusCode;
}
