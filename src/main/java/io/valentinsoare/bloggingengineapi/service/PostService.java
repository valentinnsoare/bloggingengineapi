package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    List<PostDto> getPostsByAuthorEmail(String email);
    PostDto getPostById(long id);
    PostDto getPostByTitle(String title);
    PostDto updatePost(long id, PostDto postDto);
    void deletePost(long id);
    void deleteAllPosts();
    long countAllPosts();
    long countPostByAuthorEmail(String email);
    List<PostDto> getPostsByAuthorId(long id);
    List<PostDto> getPostsByAuthorLastName(String lastName);
    List<PostDto> getPostsByAuthorsEmail(List<String> authorsEmail);
}
