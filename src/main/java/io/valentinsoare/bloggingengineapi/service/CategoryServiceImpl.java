package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.CategoryDto;
import io.valentinsoare.bloggingengineapi.entity.Category;
import io.valentinsoare.bloggingengineapi.entity.Post;
import io.valentinsoare.bloggingengineapi.exception.NoElementsException;
import io.valentinsoare.bloggingengineapi.exception.ResourceAlreadyExists;
import io.valentinsoare.bloggingengineapi.exception.ResourceNotFoundException;
import io.valentinsoare.bloggingengineapi.repository.CategoryRepository;
import io.valentinsoare.bloggingengineapi.response.CategoryResponse;
import io.valentinsoare.bloggingengineapi.utilities.AuxiliaryMethods;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final AuxiliaryMethods auxiliaryMethods;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.auxiliaryMethods = AuxiliaryMethods.getInstance();
    }

    private CategoryDto mapToDTO(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByIds(List<Long> categoryIds) {
        List<Category> byIdIn = categoryRepository.findByIdIn(categoryIds);

        if (!byIdIn.isEmpty()) {
            return byIdIn.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .toList();
        }

        throw new ResourceNotFoundException("categories", Map.of("ids", categoryIds.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", Map.of("id", id.toString())));

        return modelMapper.map(foundCategory, CategoryDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String name) {
        Category foundCategory = categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("category", Map.of("name", name)));

        return modelMapper.map(foundCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category newCategory = modelMapper.map(categoryDto, Category.class);

        categoryRepository.findCategoryByName(newCategory.getName())
                .ifPresent(category -> {
                    throw new ResourceAlreadyExists("category", Map.of("name", category.getName()));
                });

        Category savedCategory = categoryRepository.save(newCategory);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", Map.of("id", id.toString())));

        categoryRepository.delete(foundCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto category) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", Map.of("id", id.toString())));

        foundCategory.setName(auxiliaryMethods.updateIfPresent(category.getName(), foundCategory.getName()))
                .setDescription(auxiliaryMethods.updateIfPresent(category.getDescription(), foundCategory.getDescription()))
                .setPosts((Set<Post>) auxiliaryMethods.updateIfPresent(category.getAllPostsWithCategory(), foundCategory.getPosts()));

        Category savedCategory = categoryRepository.save(foundCategory);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Category> pageWithCategories = categoryRepository.findAll(pageCharacteristics);

        List<CategoryDto> content = pageWithCategories.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        if (content.isEmpty()) {
            throw new NoElementsException("categories for page number: %s with max %s categories per page".formatted(pageNo, pageSize));
        }

        return CategoryResponse.builder()
                .pageContent(content)
                .pageNo(pageCharacteristics.getPageNumber())
                .pageSize(pageCharacteristics.getPageSize())
                .totalPostsOnPage(content.size())
                .totalPages(pageWithCategories.getTotalPages())
                .isLast(pageWithCategories.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllCategories() {
        long c = categoryRepository.count();

        if (c == 0) {
            throw new NoElementsException("categories");
        }

        return c;
    }

    @Override
    @Transactional
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }
}
