package io.valentinsoare.bloggingengineapi.repository;

import io.valentinsoare.bloggingengineapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE email = :email)")
    Page<Post> getAllPostsByAuthorEmail(String email, Pageable pageable);

    @EntityGraph(value = "post-with-authors-categories-comments", type = EntityGraph.EntityGraphType.LOAD)
    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE title = :title")
    Post getPostByTitle(String title);

    @EntityGraph(value = "post-with-authors-categories-comments", type = EntityGraph.EntityGraphType.LOAD)
    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE title = :title")
    Post getPostById(Long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM post WHERE author_id IN (SELECT id FROM author WHERE email = :email)")
    Long countPostByAuthorEmail(String email);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM post WHERE author_id = :id")
    Long countPostByAuthorId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE author_id = :id")
    void deleteAllByAuthorId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE author_id IN (SELECT id FROM author WHERE email = :email)")
    void deleteAllByAuthorEmail(String email);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE author_id = :authorId AND id = :postId")
    void deletePostByAuthorIdAndPostId(Long authorId, Long postId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE author_id IN (SELECT id FROM author WHERE first_name = :firstName AND last_name = :lastName)")
    void deleteAllPostsByAuthorFirstNameAndLastName(String firstName, String lastName);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE author_id IN (SELECT id FROM author WHERE last_name = :lastName)")
    void deleteAllPostsByAuthorLastName(String lastName);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id = :id")
    Page<Post> getAllPostsByAuthorId(Long id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE first_name = :firstName)")
    Page<Post> getAllPostsByAuthorLastName(String firstName, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE first_name = :firstName AND last_name = :lastName)"
    )
    Page<Post> getAllPostsByAuthorFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE category_id IN (SELECT id FROM category WHERE name = :categoryName)")
    Page<Post> getAllPostsByCategoryName(String categoryName, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE category_id = :categoryId")
    Page<Post> getAllPostsByCategoryId(Long categoryId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM post WHERE category_id IN (SELECT id FROM category WHERE name = :categoryName)")
    Long countPostsByCategoryName(String categoryName);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM post WHERE category_id = :categoryId")
    Long countPostByCategoryId(Long categoryId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE category_id = :categoryId")
    void deleteAllPostsByCategoryId(Long categoryId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post WHERE category_id = :categoryId AND id = :postId")
    void deletePostByCategoryIdAndPostId(Long categoryId, Long postId);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM post WHERE category_id IN (SELECT id FROM category WHERE name = :categoryName)"
    )
    void deleteAllPostsByCategoryName(String categoryName);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE last_name = :lastName) AND category_id IN (SELECT id FROM category WHERE name = :categoryName)")
    Page<Post> getAllPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE first_name = :firstName AND last_name = :lastName) AND category_id IN (SELECT id FROM category WHERE name = :categoryName)"
    )
    Page<Post> getPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM post WHERE author_id IN (SELECT id FROM author WHERE email = :email) AND category_id IN (SELECT id FROM category WHERE name = :categoryName)"
    )
    Page<Post> getPostsByAuthorEmailAndCategoryName(String email, String categoryName, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM post WHERE author_id = :authorId AND category_id = :categoryId"
    )
    Page<Post> getPostsByAuthorIdAndCategoryId(Long authorId, Long categoryId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM post WHERE author_id IN (SELECT id FROM author WHERE last_name = :lastName) AND category_id IN (SELECT id FROM category WHERE name = :categoryName)"
    )
    Long countPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName);

}