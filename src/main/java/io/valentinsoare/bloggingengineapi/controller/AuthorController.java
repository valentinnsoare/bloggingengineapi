package io.valentinsoare.bloggingengineapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import io.valentinsoare.bloggingengineapi.response.AuthorResponse;
import io.valentinsoare.bloggingengineapi.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "These endpoints are for managing authors.")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MAINTAINER')")
    @Operation(
            summary = "Create an author",
            description = "Create a new author."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Author created successfully."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody @Valid AuthorDto authorDto) {
        AuthorDto author = authorService.createAuthor(authorDto);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get author by ID",
            description = "Get author by ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Author retrieved successfully."
    )
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping("/email")
    @Operation(
            summary = "Get author by email",
            description = "Get author by email."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Author retrieved successfully."
    )
    public ResponseEntity<AuthorDto> getAuthorByEmail(@RequestParam String email) {
        AuthorDto author = authorService.getAuthorByEmail(email);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }


    @GetMapping("/{firstName}")
    @Operation(
            summary = "Get authors by first name",
            description = "Get authors by first name."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authors retrieved successfully."
    )
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
    @Operation(
            summary = "Get authors by last name",
            description = "Get authors by last name."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authors retrieved successfully."
    )
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
    @Operation(
            summary = "Check if author exists by email",
            description = "Check if author exists by email."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Author exists."
    )
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = authorService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all authors",
            description = "Get all authors."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authors retrieved successfully."
    )
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
    @Operation(
            summary = "Count authors",
            description = "Count authors."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authors counted successfully."
    )
    public ResponseEntity<Long> countAuthors() {
        Long count = authorService.countAuthors();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
