package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.AuthorResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorByEmail(String email);
    AuthorResponse getAuthorsByFirstName(String firstName, int pageNo, int pageSize, String sortBy, String sortDir);
    AuthorResponse getAuthorsByLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    boolean existsByEmail(String email);
    AuthorDto createAuthor(AuthorDto authorDto);
    AuthorDto updateAuthor(AuthorDto authorDto);
    List<PostDto> updateAuthorPostList(Long id, List<Long> postIds);
    void deleteAuthor(Long id);
    AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir);
    Long countAuthors();
}
