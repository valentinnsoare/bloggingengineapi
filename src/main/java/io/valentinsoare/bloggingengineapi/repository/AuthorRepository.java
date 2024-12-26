package io.valentinsoare.bloggingengineapi.repository;

import io.valentinsoare.bloggingengineapi.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM author WHERE id = :id")
    @EntityGraph(value = "author-with-posts", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Author> getAuthorById(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM author WHERE email = :email")
    @EntityGraph(value = "author-with-posts", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Author> getAuthorByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM author WHERE first_name = :firstName")
    Page<Author> getAuthorsByFirstName(String firstName, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM author WHERE last_name = :lastName")
    Page<Author> getAuthorsByLastName(String lastName, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT 1 FROM author WHERE email = :email)")
    Boolean existsByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM author")
    Page<Author> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM author WHERE first_name = :firstName AND last_name = :lastName")
    Page<Author> getAuthorsByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
}
