package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPostById(Long id);
    PostDto getPostByTitle(String title);
    PostDto updatePost(Long id, PostDto postDto);
    PostDto updatePostByTitle(String title, PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    void deletePostWithId(Long id);
    void deleteAllPosts();
    Long countAllPosts();

    PostResponse getPostsByAuthorId(Long authorId, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorEmail(String email, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countPostsByAuthorId(Long authorId);
    void deleteAllPostsByAuthorId(Long authorId);

    PostResponse getPostsByCategoryName(String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countPostByCategoryId(Long categoryId);
    void deleteAllPostsByCategoryId(Long categoryId);
    void deleteAllPostsByCategoryName(String categoryName);

    PostResponse getPostsByAuthorEmailAndCategoryName(String email, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getPostsByAuthorIdAndCategoryId(Long authorId, Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
}
