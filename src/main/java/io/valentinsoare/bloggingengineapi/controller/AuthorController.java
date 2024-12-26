package io.valentinsoare.bloggingengineapi.controller;

import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.AuthorResponse;
import io.valentinsoare.bloggingengineapi.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody @Valid AuthorDto authorDto) {
        AuthorDto author = authorService.createAuthor(authorDto);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<AuthorDto> getAuthorByEmail(@RequestParam String email) {
        AuthorDto author = authorService.getAuthorByEmail(email);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<AuthorResponse> getAuthorsByFirstName(
            @PathVariable @NotNull String firstName,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {;
        AuthorResponse authorsByFirstName = authorService.getAuthorsByFirstName(firstName, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(authorsByFirstName, HttpStatus.OK);
    }

    @GetMapping("/{lastName}")
    public ResponseEntity<AuthorResponse> getAuthorsByLastName(
            @PathVariable @NotNull String lastName,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        AuthorResponse authorsByLastName = authorService.getAuthorsByLastName(lastName, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(authorsByLastName, HttpStatus.OK);
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = authorService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody @Valid AuthorDto authorDto) {
        AuthorDto author = authorService.updateAuthor(authorDto);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PutMapping("/{id}/posts")
    public ResponseEntity<List<PostDto>> updateAuthorPostsList(@PathVariable Long id, @RequestBody List<Long> postIds) {
        List<PostDto> updatedAuthorPosts = authorService.updateAuthorPostList(id, postIds);
        return new ResponseEntity<>(updatedAuthorPosts, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<AuthorResponse> getAllAuthors(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        AuthorResponse authors = authorService.getAllAuthors(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAuthors() {
        Long count = authorService.countAuthors();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
