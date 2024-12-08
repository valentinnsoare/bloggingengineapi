package io.valentinsoare.bloggingengineapi.dto;

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
public class PostDto {
    private long id;

    @NotEmpty(message = "Title is mandatory!")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters!")
    private String title;

    @NotEmpty(message = "Author is mandatory!")
    @Size(min = 1, max = 50, message = "Author must be between 1 and 50 characters!")
    private AuthorDto author;

    @NotEmpty(message = "Description is mandatory!")
    @Size(min = 1, max = 355, message = "Description must be between 1 and 355 characters!")
    private String description;

    @NotEmpty(message = "Category is mandatory!")
    private CategoryDto category;

    @NotEmpty(message = "Content is mandatory!")
    @Size(min = 1, max = 15000, message = "Content must be between 1 and 15000 characters!")
    private String content;

    private Set<CommentDto> comments = Collections.emptySet();

    private Set<CategoryDto> categories = Collections.emptySet();

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", content='" + content + '\'' +
                '}';
    }
}
