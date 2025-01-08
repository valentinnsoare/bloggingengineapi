package io.valentinsoare.bloggingengineapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Response object for Author")
public class AuthorResponse {
    @NotNull
    @Schema(description = "Authors on page", example = "[AuthorDto]")
    private List<AuthorDto> pageContent;

    @Schema(description = "Page number", example = "0")
    @Min(value = 0, message = "Page number must be greater than or equal to 0!")
    private int pageNo;

    @Schema(description = "Page size", example = "10")
    @Min(value = 1, message = "Page size must be greater than or equal to 1!")
    private int pageSize;

    @Schema(description = "Total authors on page", example = "10")
    @Min(value = 0, message = "Total authors on page must be greater than or equal to 0!")
    private long totalAuthorsOnPage;

    @Schema(description = "Total pages", example = "10")
    @Min(value = 1, message = "Total pages must be greater than or equal to 1!")
    private int totalPages;

    @NotNull
    @Schema(description = "Is first page", example = "true")
    private boolean isLast;
}
