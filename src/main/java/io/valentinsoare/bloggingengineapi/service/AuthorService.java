package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.AuthorResponse;
import io.valentinsoare.bloggingengineapi.response.PostResponse;

import java.util.List;

public interface AuthorService {
    AuthorResponse getAuthorsByFirstName(String firstName, int pageNo, int pageSize, String sortBy, String sortDir);
    AuthorResponse getAuthorsByLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir);
    AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir);

    boolean existsByEmail(String email);
    boolean existsById(Long id);
    boolean existsByFirstName(String firstName);
    boolean existsByLastName(String lastName);

    AuthorDto createAuthor(AuthorDto authorDto);
    AuthorDto updateAuthorById(Long id, AuthorDto authorDto);
    AuthorDto updateAuthorByEmail(String email, AuthorDto authorDto);
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorByEmail(String email);

    PostResponse getAuthorPostsList(Long id);
    PostResponse getAuthorPostsListByEmail(String email);
    PostResponse updateAuthorsPostsList(Long id, List<PostDto> posts);
    PostResponse createAuthorPostsList(Long id, List<PostDto> posts);

    void deleteAuthorById(Long id);
    void deleteAuthorByEmail(String email);
    void deleteAuthorsPostsList(Long id);

    Long countAuthors();
    Long countAuthorsByFirstName(String firstName);
    Long countAuthorsByLastName(String lastName);
    Long countHowManyPostsAuthorHasById(Long id);
    Long countHowManyPostsAuthorHasByEmail(String email);
    Long countHowManyPostsAuthorHasByFirstName(String firstName);
    Long countHowManyPostsAuthorHasByLastName(String lastName);
}
