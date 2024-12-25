package io.valentinsoare.bloggingengineapi.repository;

import io.valentinsoare.bloggingengineapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE email = :email)")
    Page<Post> getAllPostsByAuthorEmail(String email, Pageable pageable);

    @EntityGraph(value = "post-with-authors-categories-comments", type = EntityGraph.EntityGraphType.LOAD)
    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE title = :title")
    Post getPostByTitle(String title);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM post WHERE author_id IN (SELECT id FROM author WHERE email = :email)")
    Long countPostByAuthorEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id = :id")
    Page<Post> getAllPostsByAuthorId(Long id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE first_name = :firstName)")
    Page<Post> getAllPostsByAuthorLastName(String firstName, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE first_name = :firstName AND last_name = :lastName)")
    Page<Post> getAllPostsByAuthorFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

    Page<Post> findAll(Pageable pageable);
}