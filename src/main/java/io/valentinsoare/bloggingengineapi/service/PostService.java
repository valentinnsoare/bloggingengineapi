package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(long id);
    PostDto getPostByTitle(String title);
    PostDto updatePost(long id, PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    void deletePost(long id);
    void deleteAllPosts();
    long countAllPosts();

    PostResponse getPostsByAuthorId(long authorId, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorFirstNameAndLastName(String firstName, String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorEmail(String email, int pageNo, int pageSize, String sortBy, String sortDir);
    long countPostByAuthorEmail(String email);
    long countPostsByAuthorId(long authorId);
    void deleteAllPostsByAuthorId(long authorId);
    void deletePostByAuthorIdAndPostId(long authorId, long postId);
    void deleteAllPostsByAuthorEmail(String email);
    void deleteAllPostsByAuthorFirstNameAndLastName(String firstName, String lastName);
    void deleteAllPostsByAuthorLastName(String lastName);

    PostResponse getPostsByCategoryName(String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByCategoryId(long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    long countPostByCategoryName(String categoryName);
    long countPostByCategoryId(long categoryId);
    void deleteAllPostsByCategoryId(long categoryId);
    void deletePostByCategoryIdAndPostId(long categoryId, long postId);
    void deleteAllPostsByCategoryName(String categoryName);

    PostResponse getPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorEmailAndCategoryName(String email, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorIdAndCategoryId(long authorId, long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    long countPostByAuthorLastNameAndCategoryName(String lastName, String categoryName);
    long countPostByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName);
    long countPostByAuthorEmailAndCategoryName(String email, String categoryName);
    long countPostByAuthorIdAndCategoryId(long authorId, long categoryId);
    void deleteAllPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName);
    void deleteAllPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName);
}
