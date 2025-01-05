package io.valentinsoare.bloggingengineapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.valentinsoare.bloggingengineapi.dto.CommentDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "CommentResponse", description = "Response object for Comment", hidden = true)
public class CommentResponse {
    @NotNull
    private List<CommentDto> comments;

    @Min(value = 0, message = "Page number must be greater than or equal to 0!")
    private int pageNo;

    @Min(value = 1, message = "Page size must be greater than or equal to 1!")
    private int pageSize;

    @Min(value = 0, message = "Total comments on page must be greater than or equal to 0!")
    private long totalCommentsOnPage;

    @Min(value = 1, message = "Total pages must be greater than or equal to 1!")
    private int totalPages;

    @NotNull
    private boolean isLast;
}
