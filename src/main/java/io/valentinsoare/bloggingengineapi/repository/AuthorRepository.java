package io.valentinsoare.bloggingengineapi.repository;

import io.valentinsoare.bloggingengineapi.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findById(Long id);
    Optional<Author> findByEmail(String email);
    Optional<Author> findByFirstName(String firstName);
    Optional<Author> findByLastName(String lastName);
    List<Author> findByIdIn(List<Long> userIds);
    Boolean existsByEmail(String email);

    @EntityGraph(value = "author-with-posts", type = EntityGraph.EntityGraphType.LOAD)
    Page<Author> findAll(Pageable pageable);
}
