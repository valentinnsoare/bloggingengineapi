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
@Schema(name = "AuthorResponse", description = "Response object for Author", hidden = true)
public class AuthorResponse {
    @NotNull
    private List<AuthorDto> pageContent;

    @Min(value = 0, message = "Page number must be greater than or equal to 0!")
    private int pageNo;

    @Min(value = 1, message = "Page size must be greater than or equal to 1!")
    private int pageSize;

    @Min(value = 0, message = "Total authors on page must be greater than or equal to 0!")
    private long totalAuthorsOnPage;

    @Min(value = 1, message = "Total pages must be greater than or equal to 1!")
    private int totalPages;

    @NotNull
    private boolean isLast;
}
