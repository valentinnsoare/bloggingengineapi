package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Collections;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthorDto", description = "Data Transfer Object for Author", hidden = true)
public class AuthorDto {
    @Schema(description = "ID of the author", example = "1")
    private long id;

    @NotBlank(message = "First name is mandatory!")
    @Schema(description = "First name of the author", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is mandatory!")
    @Schema(description = "Last name of the author", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Email must be a valid email address!")
    @Schema(description = "Email of the author", example = "unknown@gmail.com")
    private String email;

    @Schema(description = "Posts from the author")
    private Set<PostDto> postsFromAuthor = Collections.emptySet();

    @Override
    public String toString() {
        return "AuthorDto:[" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ']';
    }
}
