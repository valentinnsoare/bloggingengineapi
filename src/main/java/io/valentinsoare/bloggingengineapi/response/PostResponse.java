package io.valentinsoare.bloggingengineapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@Schema(name = "PostResponse", description = "Response object for Post", hidden = true)
public class PostResponse {
    @NotNull
    private List<PostDto> pageContent;

    @Min(value = 0, message = "Page number must be greater than or equal to 0!")
    private int pageNo;

    @Min(value = 1, message = "Page size must be greater than or equal to 1!")
    private int pageSize;

    @Min(value = 0, message = "Total posts on page must be greater than or equal to 0!")
    private long totalPostsOnPage;

    @Min(value = 1, message = "Total pages must be greater than or equal to 1!")
    private int totalPages;

    @NotNull
    private boolean isLast;
}
