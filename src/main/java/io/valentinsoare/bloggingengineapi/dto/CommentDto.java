package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommentDto", description = "Data Transfer Object for Comment", hidden = true)
public class CommentDto {
    @Schema(description = "Id of the comment", example = "1")
    private long id;

    @NotEmpty(message = "Name is mandatory!")
    @Schema(description = "Name of the comment author", example = "John")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters!")
    private String name;

    @NotEmpty(message = "Email is mandatory!")
    @Email(message = "Email must be a valid email address!")
    @Schema(description = "Email of the comment author", example = "unknown@gmail.com")
    @Size(min = 1, max = 50, message = "Email must be between 1 and 50 characters!")
    private String email;

    @NotEmpty(message = "Comment body is mandatory!")
    @Schema(description = "Body of the comment", example = "Comment body")
    @Size(min = 1, max = 5000, message = "Comment body must be between 1 and 5000 characters!")
    private String body;

    @Schema(description = "Id of the post", example = "1")
    private long postId;

    @Override
    public String toString() {
        return "CommentDto: [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                ", postId=" + postId +
                ']';
    }
}
