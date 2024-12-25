package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.CategoryDto;
import io.valentinsoare.bloggingengineapi.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    boolean existsByName(String name);
    List<CategoryDto> getCategoriesByIds(List<Long> categoryIds);
    CategoryDto getCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    CategoryDto addCategory(CategoryDto category);
    void deleteCategory(Long id);
    CategoryDto updateCategory(Long id, CategoryDto category);
    CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);
    long countAllCategories();
    void deleteAllCategories();
}
