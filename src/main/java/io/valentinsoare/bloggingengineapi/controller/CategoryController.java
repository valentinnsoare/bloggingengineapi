package io.valentinsoare.bloggingengineapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.valentinsoare.bloggingengineapi.dto.CategoryDto;
import io.valentinsoare.bloggingengineapi.response.CategoryResponse;
import io.valentinsoare.bloggingengineapi.service.CategoryService;
import io.valentinsoare.bloggingengineapi.utilities.ApplicationConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Managing categories.")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @Operation(
            summary = "Create a category",
            description = "Create a new category."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Category created successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.CREATED);
     }

    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Get all categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully."
    )
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by id",
            description = "Get a category by id."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category retrieved successfully."
    )
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/name")
    @Operation(
            summary = "Get category by name",
            description = "Get a category by name."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category retrieved successfully."
    )
    public ResponseEntity<CategoryDto> getCategoryByName(@RequestParam String name) {
        return new ResponseEntity<>(categoryService.getCategoryByName(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @Operation(
            summary = "Update a category",
            description = "Update a category."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category updated successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Delete a category",
            description = "Delete a category."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category deleted successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully!", HttpStatus.OK);
    }

    @GetMapping("/exists")
    @Operation(
            summary = "Check if category exists by name",
            description = "Check if a category exists by name."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category exists."
    )
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        return new ResponseEntity<>(categoryService.existsByName(name), HttpStatus.OK);
    }

    @GetMapping("/count")
    @Operation(
            summary = "Count all categories",
            description = "Count all categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories counted successfully."
    )
    public ResponseEntity<Long> countCategories() {
        return new ResponseEntity<>(categoryService.countAllCategories(), HttpStatus.OK);
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Delete all categories",
            description = "Delete all categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "All categories deleted successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return new ResponseEntity<>("All categories deleted successfully!", HttpStatus.OK);
    }
}
