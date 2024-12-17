package io.valentinsoare.bloggingengineapi.controller;

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
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.CREATED);
     }

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_CATEGORIES_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/name")
    public ResponseEntity<CategoryDto> getCategoryByName(@RequestParam String name) {
        return new ResponseEntity<>(categoryService.getCategoryByName(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully!", HttpStatus.OK);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        return new ResponseEntity<>(categoryService.existsByName(name), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countCategories() {
        return new ResponseEntity<>(categoryService.countAllCategories(), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return new ResponseEntity<>("All categories deleted successfully!", HttpStatus.OK);
    }
}
