package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PostDto", description = "Data Transfer Object for Post", hidden = true)
public class PostDto {
    private long id;

    @NotEmpty(message = "Title is mandatory!")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters!")
    private String title;

    @NotEmpty(message = "Description is mandatory!")
    @Size(min = 1, max = 355, message = "Description must be between 1 and 355 characters!")
    private String description;

    @NotEmpty(message = "Content is mandatory!")
    @Size(min = 1, max = 15000, message = "Content must be between 1 and 15000 characters!")
    private String content;

    @Size(min = 1, max = 5, message = "We need at least one author and maximum is five!")
    private Set<AuthorDto> authors = Collections.emptySet();

    private Set<CommentDto> comments = Collections.emptySet();

    @Size(min = 1, max = 5, message = "We need at least one category and maximum is five!")
    private Set<CategoryDto> categories = Collections.emptySet();

    @Override
    public String toString() {
        return "PostDto: [" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", description='" + description + '\'' +
                ", categories=" + categories +
                ", content='" + content + '\'' +
                ']';
    }
}
