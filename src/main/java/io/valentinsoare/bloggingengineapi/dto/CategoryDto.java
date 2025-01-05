package io.valentinsoare.bloggingengineapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "CategoryDto", description = "Data Transfer Object for Category", hidden = true)
public class CategoryDto {
    @Schema(description = "Id of the category", example = "1")
    private Long id;

    @NotBlank(message = "Name is mandatory!")
    @Schema(description = "Name of the category", example = "Category name")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters!")
    private String name;

    @NotBlank(message = "Description is mandatory!")
    @Schema(description = "Description of the category", example = "Category description")
    @Size(min = 1, max = 355, message = "Description must be between 1 and 355 characters!")
    private String description;

    @Schema(description = "All posts with this category", example = "[PostDto]")
    private Set<PostDto> allPostsWithCategory = Collections.emptySet();

    @Override
    public String toString() {
        return "CategoryDto: [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ']';
    }
}
