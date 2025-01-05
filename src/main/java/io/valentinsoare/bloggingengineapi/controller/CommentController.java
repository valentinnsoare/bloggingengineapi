package io.valentinsoare.bloggingengineapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.valentinsoare.bloggingengineapi.dto.CommentDto;
import io.valentinsoare.bloggingengineapi.response.CommentResponse;
import io.valentinsoare.bloggingengineapi.service.CommentService;
import io.valentinsoare.bloggingengineapi.utilities.ApplicationConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Endpoints for comments.")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/posts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @Operation(
            summary = "Create a comment",
            description = "Create a comment for a post."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Comment created successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CommentDto> createComment(@PathVariable  Long postId, @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}")
    @Operation(
            summary = "Get all comments by post id",
            description = "Get all comments for a post."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments retrieved successfully."
    )
    public ResponseEntity<CommentResponse> getAllCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_COMMENTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_COMMENTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(commentService.getAllCommentsByPostId(postId, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{commentId}/posts/{postId}")
    @Operation(
            summary = "Get comment by id and post id",
            description = "Get a comment by id and post id."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment retrieved successfully."
    )
    public ResponseEntity<CommentDto> getCommentByIdAndPostId(@PathVariable Long postId, @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.getCommentByIdAndPostId(commentId, postId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}/posts/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @Operation(
            summary = "Update a comment",
            description = "Update a comment for a post."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment updated successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateCommentByIdAndPostId(commentId, postId, commentDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{commentId}/posts/{postId}")
    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment for a post."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Comment deleted successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteCommentById(commentId, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
