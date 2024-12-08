package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.CategoryDto;
import io.valentinsoare.bloggingengineapi.entity.Category;
import io.valentinsoare.bloggingengineapi.entity.Post;
import io.valentinsoare.bloggingengineapi.exception.NoElementsException;
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
        log.info("Checking if category with name {} exists.", name);

        if (categoryRepository.existsByName(name)) {
            log.info("Category with name {} exists.", name);
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByIds(List<Long> categoryIds) {
        log.info("Searching for categories with ids: {}.", categoryIds);
        List<Category> byIdIn = categoryRepository.findByIdIn(categoryIds);

        if (!byIdIn.isEmpty()) {
            log.info("Found categories with ids: {}.", categoryIds);

            return byIdIn.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .toList();
        }

        log.info("Categories with ids {} not found.", categoryIds);
        throw new ResourceNotFoundException("categories", Map.of("ids", categoryIds.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        log.info("Searching for category with id: {}.", id);

        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with id {} not found.", id);
                    return new ResourceNotFoundException("category", Map.of("id", id.toString()));
                });

        log.info("Fetched category with id {}.", id);

        CategoryDto categoryDto = modelMapper.map(foundCategory, CategoryDto.class);
        log.info("Mapped category with id {} to categoryDto.", id);

        return categoryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String name) {
        log.info("Searching for category with name: {}.", name);

        Category foundCategory = categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> {
                    log.error("Category with name {} not found.", name);
                    return new ResourceNotFoundException("category", Map.of("name", name));
                });

        log.info("Fetched category with name {}.", name);

        CategoryDto categoryDto = modelMapper.map(foundCategory, CategoryDto.class);
        log.info("Mapped category with name {} to categoryDto.", name);

        return categoryDto;
    }

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto category) {
        Category newCategory = modelMapper.map(category, Category.class);
        log.info("Mapped categoryDto to category with name: {}.", category.getName());

        System.out.println(newCategory);

        categoryRepository.findCategoryByName(newCategory.getName())
                .ifPresentOrElse(
                        foundCategory -> log.error("Category with name {} already exists.", foundCategory.getName()),
                        () -> {
                            log.info("Saving category with name {}.", newCategory.getName());
                            categoryRepository.save(newCategory);
                        }
                );

        Category savedCategory = categoryRepository.save(newCategory);

        System.out.println(savedCategory);
        log.info("Category with name {} saved.", newCategory.getName());

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}.", id);

        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with id {} not found.", id);
                    return new ResourceNotFoundException("category", Map.of("id", id.toString()));
                });
        log.info("Fetched category with id {}.", id);

        log.info("Deleting category with id {}.", id);
        categoryRepository.delete(foundCategory);
        log.info("Category with id {} deleted.", id);
    }

    @Override
    @Transactional
    public void updateCategory(Long id, CategoryDto category) {
        log.info("Updating category with id: {}.", id);

        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with id {} not found.", id);
                    return new ResourceNotFoundException("category", Map.of("id", id.toString()));
                });
        log.info("Fetched category with id {}.", id);

        foundCategory.setName(auxiliaryMethods.updateIfPresent(category.getName(), foundCategory.getName()))
                .setDescription(auxiliaryMethods.updateIfPresent(category.getDescription(), foundCategory.getDescription()))
                .setPosts((Set<Post>) auxiliaryMethods.updateIfPresent(category.getAllPostsWithCategory(), foundCategory.getPosts()));

        log.info("Updated category with id {}.", id);
        categoryRepository.save(foundCategory);
        log.info("Category with id {} saved.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting all posts from page number: {} and page size: {} in sorted order.", pageNo, pageSize);

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
        log.info("Counting all categories.");
        long c = categoryRepository.count();

        if (c == 0) {
            throw new NoElementsException("categories");
        }

        log.info("Counted all categories.");
        return c;
    }

    @Override
    @Transactional
    public void deleteAllCategories() {
        log.info("Deleting all categories.");

        categoryRepository.deleteAll();

        log.info("Deleted all categories.");
    }
}
