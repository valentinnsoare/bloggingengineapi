package io.valentinsoare.bloggingengineapi.repository;

import io.valentinsoare.bloggingengineapi.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    List<Category> findByIdIn(List<Long> categoryIds);
    boolean existsByName(String name);
    Optional<Category> findCategoryByName(String name);

    @EntityGraph(value = "category-with-posts", type = EntityGraph.EntityGraphType.LOAD)
    Page<Category> findAll(Pageable pageable);
}
