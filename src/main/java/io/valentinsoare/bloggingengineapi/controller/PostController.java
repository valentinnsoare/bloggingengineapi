package io.valentinsoare.bloggingengineapi.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Posts", description = "Endpoints for managing posts.")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
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
    public ResponseEntity<PostDto> getPostById(@PathVariable @NotNull Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<PostDto> getPostByTitle(@PathVariable @NotNull String title) {
        return new ResponseEntity<>(postService.getPostByTitle(title), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
    }

    @PutMapping("/{title}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PostDto> updatePostByTitle(@PathVariable @NotNull String title, @Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.updatePostByTitle(title, postDto), HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deletePostById(@PathVariable Long id) {
        postService.deletePostWithId(id);
        return new ResponseEntity<>(String.format("Post with id: %s deleted successfully!", id), HttpStatus.OK);
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

    @DeleteMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPosts() {
        postService.deleteAllPosts();
        return new ResponseEntity<>("All Posts deleted successfully!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/author/{authorId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPostsByAuthorId(@PathVariable Long authorId) {
        postService.deleteAllPostsByAuthorId(authorId);
        return new ResponseEntity<>(String.format("All Posts with author id: %s deleted successfully!", authorId), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/author/{authorId}")
    public ResponseEntity<String> deletePostByAuthorIdAndPostId(@PathVariable Long authorId, @PathVariable Long postId) {
        postService.deletePostByAuthorIdAndPostId(authorId, postId);
        return new ResponseEntity<>(String.format("Post with id: %s and author id: %s deleted successfully!", postId, authorId), HttpStatus.OK);
    }

    @DeleteMapping("/author/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPostsByAuthorEmail(@PathVariable @NotNull String email) {
        postService.deleteAllPostsByAuthorEmail(email);
        return new ResponseEntity<>(String.format("All Posts with author email: %s deleted successfully!", email), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/author/{firstName}/{lastName}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPostsByAuthorFirstNameAndLastName(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName) {
        postService.deleteAllPostsByAuthorFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(String.format("All Posts with author first name: %s and last name: %s deleted successfully!", firstName, lastName), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/author/{lastName}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPostsByAuthorLastName(@PathVariable @NotNull String lastName) {
        postService.deleteAllPostsByAuthorLastName(lastName);
        return new ResponseEntity<>(String.format("All Posts with author last name: %s deleted successfully!", lastName), HttpStatus.OK);
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

    @GetMapping("/count/category/{categoryName}")
    public ResponseEntity<Long> countPostsByCategoryName(@PathVariable @NotNull String categoryName) {
        return new ResponseEntity<>(postService.countPostsByCategoryName(categoryName), HttpStatus.OK);
    }

    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<Long> countPostByCategoryId(@PathVariable Long categoryId) {
        return new ResponseEntity<>(postService.countPostByCategoryId(categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/category/{categoryId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPostsByCategoryId(@PathVariable Long categoryId) {
        postService.deleteAllPostsByCategoryId(categoryId);
        return new ResponseEntity<>(String.format("All Posts with category id: %s deleted successfully!", categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}/{postId}")
    public ResponseEntity<String> deletePostByCategoryIdAndPostId(@PathVariable Long categoryId, @PathVariable Long postId) {
        postService.deletePostByCategoryIdAndPostId(categoryId, postId);
        return new ResponseEntity<>(String.format("Post with id: %s and category id: %s deleted successfully!", postId, categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/category/{categoryName}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAllPostsByCategoryName(@PathVariable @NotNull String categoryName) {
        postService.deleteAllPostsByCategoryName(categoryName);
        return new ResponseEntity<>(String.format("All Posts with category name: %s deleted successfully!", categoryName), HttpStatus.OK);
    }

    @GetMapping("/author/{lastName}/category/{categoryName}")
    public ResponseEntity<PostResponse> getPostsByAuthorLastNameAndCategoryName(
            @PathVariable @NotNull String lastName,
            @PathVariable @NotNull String categoryName,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorLastNameAndCategoryName(lastName, categoryName, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{firstName}/{lastName}/category/{categoryName}")
    public ResponseEntity<PostResponse> getPostsByAuthorFirstNameAndLastNameAndCategoryName(
            @PathVariable @NotNull String firstName,
            @PathVariable @NotNull String lastName,
            @PathVariable @NotNull String categoryName,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorFirstNameAndLastNameAndCategoryName(firstName, lastName, categoryName, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{email}/category/{categoryName}")
    public ResponseEntity<PostResponse> getPostsByAuthorEmailAndCategoryName(
            @PathVariable @NotNull String email,
            @PathVariable @NotNull String categoryName,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorEmailAndCategoryName(email, categoryName, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/author/{authorId}/category/{categoryId}")
    public ResponseEntity<PostResponse> getPostsByAuthorIdAndCategoryId(
            @PathVariable Long authorId,
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNo", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstants.DEFAULT_POSTS_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApplicationConstants.DEFAULT_POSTS_SORT_DIR, required = false) String sortDir
    ) {
        return new ResponseEntity<>(postService.getPostsByAuthorIdAndCategoryId(authorId, categoryId, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/count/author/{lastName}/category/{categoryName}")
    public ResponseEntity<Long> countPostsByAuthorLastNameAndCategoryName(@PathVariable @NotNull String lastName, @PathVariable @NotNull String categoryName) {
        return new ResponseEntity<>(postService.countPostsByAuthorLastNameAndCategoryName(lastName, categoryName), HttpStatus.OK);
    }

    @GetMapping("/count/author/{firstName}/{lastName}/category/{categoryName}")
    public ResponseEntity<Long> countPostsByAuthorFirstNameAndLastNameAndCategoryName(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName, @PathVariable @NotNull String categoryName) {
        return new ResponseEntity<>(postService.countPostsByAuthorFirstNameAndLastNameAndCategoryName(firstName, lastName, categoryName), HttpStatus.OK);
    }

    @GetMapping("/count/author/{email}/category/{categoryName}")
    public ResponseEntity<Long> countPostsByAuthorEmailAndCategoryName(@PathVariable @NotNull String email, @PathVariable @NotNull String categoryName) {
        return new ResponseEntity<>(postService.countPostsByAuthorEmailAndCategoryName(email, categoryName), HttpStatus.OK);
    }

    @GetMapping("/count/author/{authorId}/category/{categoryId}")
    public ResponseEntity<Long> countPostsByAuthorIdAndCategoryId(@PathVariable Long authorId, @PathVariable Long categoryId) {
        return new ResponseEntity<>(postService.countPostsByAuthorIdAndCategoryId(authorId, categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/title/{name}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deletePostByTitle(@PathVariable @NotNull String name) {
        postService.deletePostWithTitle(name);
        return new ResponseEntity<>(String.format("Post with title : %s deleted successfully!", name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/author/{lastName}/category/{categoryName}")
    public ResponseEntity<String> deleteAllPostsByAuthorLastNameAndCategoryName(@PathVariable @NotNull String lastName, @PathVariable @NotNull String categoryName) {
        postService.deleteAllPostsByAuthorLastNameAndCategoryName(lastName, categoryName);
        return new ResponseEntity<>(String.format("All Posts with author last name: %s and category name: %s deleted successfully!", lastName, categoryName), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/author/{firstName}/{lastName}/category/{categoryName}")
    public ResponseEntity<String> deleteAllPostsByAuthorFirstNameAndLastNameAndCategoryName(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName, @PathVariable @NotNull String categoryName) {
        postService.deleteAllPostsByAuthorFirstNameAndLastNameAndCategoryName(firstName, lastName, categoryName);
        return new ResponseEntity<>(String.format("All Posts with author first name: %s, last name: %s and category name: %s deleted successfully!", firstName, lastName, categoryName), HttpStatus.OK);
    }
}
