package io.valentinsoare.bloggingengineapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;

    @NotEmpty(message = "Name is mandatory!")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters!")
    private String name;

    @NotEmpty(message = "Email is mandatory!")
    @Email(message = "Email must be a valid email address!")
    @Size(min = 1, max = 50, message = "Email must be between 1 and 50 characters!")
    private String email;

    @NotEmpty(message = "Comment body is mandatory!")
    @Size(min = 1, max = 5000, message = "Comment body must be between 1 and 5000 characters!")
    private String body;

    private long postId;

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                ", postId=" + postId +
                '}';
    }
}
