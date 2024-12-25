package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(Long id);
    PostDto getPostByTitle(String title);
    PostDto updatePost(Long id, PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    void deletePost(Long id);
    void deleteAllPosts();
    Long countAllPosts();

    PostResponse getPostsByAuthorId(Long authorId, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorFirstNameAndLastName(String firstName, String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorEmail(String email, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countPostByAuthorEmail(String email);
    Long countPostsByAuthorId(Long authorId);
    void deleteAllPostsByAuthorId(Long authorId);
    void deletePostByAuthorIdAndPostId(Long authorId, Long postId);
    void deleteAllPostsByAuthorEmail(String email);
    void deleteAllPostsByAuthorFirstNameAndLastName(String firstName, String lastName);
    void deleteAllPostsByAuthorLastName(String lastName);

    PostResponse getPostsByCategoryName(String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countPostsByCategoryName(String categoryName);
    Long countPostByCategoryId(Long categoryId);
    void deleteAllPostsByCategoryId(Long categoryId);
    void deletePostByCategoryIdAndPostId(Long categoryId, Long postId);
    void deleteAllPostsByCategoryName(String categoryName);

    PostResponse getPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorEmailAndCategoryName(String email, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorIdAndCategoryId(Long authorId, Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countPostByAuthorLastNameAndCategoryName(String lastName, String categoryName);
    Long countPostByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName);
    Long countPostByAuthorEmailAndCategoryName(String email, String categoryName);
    Long countPostByAuthorIdAndCategoryId(Long authorId, Long categoryId);
    void deleteAllPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName);
    void deleteAllPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName);
}
