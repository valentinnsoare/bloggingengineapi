package io.valentinsoare.bloggingengineapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Collections;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Name is mandatory!")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters!")
    private String name;

    @NotBlank(message = "Description is mandatory!")
    @Size(min = 1, max = 355, message = "Description must be between 1 and 355 characters!")
    private String description;

    private Set<PostDto> allPostsWithCategory = Collections.emptySet();

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
