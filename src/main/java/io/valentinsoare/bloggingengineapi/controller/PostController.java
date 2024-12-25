package io.valentinsoare.bloggingengineapi.controller;

import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.PostResponse;
import io.valentinsoare.bloggingengineapi.service.PostService;
import io.valentinsoare.bloggingengineapi.utilities.ApplicationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{email}")
    public ResponseEntity<PostResponse> getAllPostsByAuthorEmail(
            @PathVariable @NotNull String email,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorEmail(email, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<PostResponse> getAllPostsByAuthorId(
            @PathVariable Long id,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorId(id, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{lastName}")
    public ResponseEntity<PostResponse> getAllPostsByAuthorLastName(
            @PathVariable @NotNull String lastName,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorLastName(lastName, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{firstName}/{lastName}")
    public ResponseEntity<PostResponse> getAllPostsByAuthorFirstNameAndLastName(
            @PathVariable @NotNull String firstName,
            @PathVariable @NotNull String lastName,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorFirstNameAndLastName(firstName, lastName, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<PostDto> getPostByTitle(@RequestParam @NotNull String name) {
        return new ResponseEntity<>(postService.getPostByTitle(name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(String.format("Post with id: %s deleted successfully!", id),HttpStatus.NO_CONTENT);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllPosts() {
        return new ResponseEntity<>(postService.countAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/count/author/{email}")
    public ResponseEntity<Long> countPostByAuthorEmail(@PathVariable @NotNull String email) {
        return new ResponseEntity<>(postService.countPostByAuthorEmail(email), HttpStatus.OK);
    }

    @GetMapping("/count/author/{id}")
    public ResponseEntity<Long> countPostByAuthorId(@PathVariable Long id) {
        return new ResponseEntity<>(postService.countPostsByAuthorId(id), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteAllPosts() {
        postService.deleteAllPosts();
        return new ResponseEntity<>("All Posts deleted successfully!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/author/{authorId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteAllPostsByAuthorId(@PathVariable Long authorId) {
        postService.deleteAllPostsByAuthorId(authorId);
        return new ResponseEntity<>(String.format("All Posts with author id: %s deleted successfully!", authorId), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/author/{authorId}/{postId}")
    public ResponseEntity<String> deletePostByAuthorIdAndPostId(@PathVariable Long authorId, @PathVariable Long postId) {
        postService.deletePostByAuthorIdAndPostId(authorId, postId);
        return new ResponseEntity<>(String.format("Post with id: %s and author id: %s deleted successfully!", postId, authorId), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/author/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteAllPostsByAuthorEmail(@PathVariable @NotNull String email) {
        postService.deleteAllPostsByAuthorEmail(email);
        return new ResponseEntity<>(String.format("All Posts with author email: %s deleted successfully!", email), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/author/{firstName}/{lastName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteAllPostsByAuthorFirstNameAndLastName(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName) {
        postService.deleteAllPostsByAuthorFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(String.format("All Posts with author first name: %s and last name: %s deleted successfully!", firstName, lastName), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/author/{lastName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteAllPostsByAuthorLastName(@PathVariable @NotNull String lastName) {
        postService.deleteAllPostsByAuthorLastName(lastName);
        return new ResponseEntity<>(String.format("All Posts with author last name: %s deleted successfully!", lastName), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<PostResponse> getAllPostsByCategoryName(
            @PathVariable @NotNull String categoryName,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByCategoryName(categoryName, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PostResponse> getAllPostsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByCategoryId(categoryId, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
}
