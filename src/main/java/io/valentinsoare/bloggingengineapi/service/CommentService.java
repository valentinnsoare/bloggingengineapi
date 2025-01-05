package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.CommentDto;
import io.valentinsoare.bloggingengineapi.response.CommentResponse;


public interface CommentService {
    CommentDto getCommentByIdAndPostId(Long commentId, Long postId);
    CommentResponse getAllCommentsByPostId(Long postId, int pageNo, int pageSize, String sortBy, String sortDir);
    CommentResponse getAllCommentsByPostTitle(String postTitle, int pageNo, int pageSize, String sortBy, String sortDir);
    CommentDto createComment(Long postId, CommentDto commentDto);
    CommentDto updateCommentByIdAndPostId(Long commentId, Long postId, CommentDto commentDto);

    void deleteCommentById(Long commentId, Long postId);
    Long countAllCommentsByPostId(Long postId);
    void deleteAllCommentsByPostId(Long postId);

    CommentResponse getAllCommentsByEmail(String email, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countAllCommentsByEmail(String email);
    void deleteAllCommentsByEmail(String email);

    CommentResponse getAllCommentsByName(String name, int pageNo, int pageSize, String sortBy, String sortDir);
    Long countAllCommentsByName(String name);
    void deleteAllCommentsByName(String name);
}
