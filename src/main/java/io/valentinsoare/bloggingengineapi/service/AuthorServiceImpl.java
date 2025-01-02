package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.entity.Author;
import io.valentinsoare.bloggingengineapi.exception.BloggingEngineException;
import io.valentinsoare.bloggingengineapi.exception.NoElementsException;
import io.valentinsoare.bloggingengineapi.exception.ResourceNotFoundException;
import io.valentinsoare.bloggingengineapi.repository.AuthorRepository;
import io.valentinsoare.bloggingengineapi.repository.PostRepository;
import io.valentinsoare.bloggingengineapi.response.AuthorResponse;
import io.valentinsoare.bloggingengineapi.response.PostResponse;
import io.valentinsoare.bloggingengineapi.utilities.AuxiliaryMethods;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final AuxiliaryMethods auxiliaryMethods;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             ModelMapper modelMapper,
                             PostRepository postRepository) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
        this.auxiliaryMethods = AuxiliaryMethods.getInstance();
        this.postRepository = postRepository;
    }

    private AuthorDto mapToDTO(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    private Author mapToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    private AuthorResponse createAndReturnAuthorResponse(Page<Author> pageWithAuthors) {
        List<AuthorDto> content = pageWithAuthors.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        return AuthorResponse.builder()
                .pageContent(content)
                .pageNo(pageWithAuthors.getNumber())
                .pageSize(pageWithAuthors.getSize())
                .totalAuthorsOnPage(content.size())
                .totalPages(pageWithAuthors.getTotalPages())
                .isLast(pageWithAuthors.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getAuthorById(Long id) {
        Author foundAuthor = authorRepository.getAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("author", Map.of("id", id.toString())));

        return mapToDTO(foundAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getAuthorByEmail(String email) {
        Author foundAuthor = authorRepository.getAuthorByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("author", Map.of("email", email)));

        AuthorDto newAuthorFound = modelMapper.map(foundAuthor, AuthorDto.class);

        foundAuthor.getAllPosts().forEach(post -> {
            PostDto newPost = modelMapper.map(post, PostDto.class);
            newAuthorFound.getPostsFromAuthor().add(newPost);
        });

        return newAuthorFound;
    }

    @Override
    public PostResponse getAuthorPostsList(Long id) {
        return null;
    }

    @Override
    public PostResponse getAuthorPostsListByEmail(String email) {
        return null;
    }

    @Override
    public PostResponse updateAuthorsPostsList(Long id, List<PostDto> posts) {
        return null;
    }


    @Override
    public PostResponse createAuthorPostsList(Long id, List<PostDto> posts) {
        return null;
    }

    @Override
    public void deleteAuthorByEmail(String email) {

    }

    @Override
    public void deleteAuthorsPostsList(Long id) {

    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAuthorsByFirstName(String firstName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Author> pageWithAuthors = authorRepository.getAuthorsByFirstName(firstName, pageCharacteristics);

        if (pageWithAuthors.isEmpty()) {
            throw new NoElementsException(
                    String.format("authors with first name: %s for page number: %s with max %s authors per page", firstName, pageNo, pageSize)
            );
        }

        return createAndReturnAuthorResponse(pageWithAuthors);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAuthorsByLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Author> pageWithAuthors = authorRepository.getAuthorsByLastName(lastName, pageCharacteristics);

        if (pageWithAuthors.isEmpty()) {
            throw new NoElementsException(
                    String.format("authors with last name: %s for page number: %s with max %s authors per page", lastName, pageNo, pageSize)
            );
        }

        return createAndReturnAuthorResponse(pageWithAuthors);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.info("Checking if author with email {} exists.", email);

        if (Boolean.TRUE.equals(authorRepository.existsByEmail(email))) {
            log.info("Author with email {} exists.", email);
            return true;
        }

        log.info("Author with email {} does not exist.", email);
        return false;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public boolean existsByFirstName(String firstName) {
        return false;
    }

    @Override
    public boolean existsByLastName(String lastName) {
        return false;
    }

    @Override
    @Transactional
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = modelMapper.map(authorDto, Author.class);
        String emailToBeSearch = author.getEmail();

        authorRepository.getAuthorByEmail(author.getEmail())
                .ifPresent(a -> {
                    throw new BloggingEngineException("author", "email already exists", Map.of("email", emailToBeSearch));
                });

        try {
            author = authorRepository.save(author);
        } catch (Exception e) {
            throw new BloggingEngineException("author", "error creating", Map.of("author", authorDto.toString()));
        }

        return modelMapper.map(author, AuthorDto.class);
    }

    @Override
    public AuthorDto updateAuthorById(Long id, AuthorDto authorDto) {
        return null;
    }

    @Override
    public AuthorDto updateAuthorByEmail(String email, AuthorDto authorDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteAuthorById(Long id) {
        log.info("Deleting author with id: {}.", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Author with id {} not found.", id);
                    throw new ResourceNotFoundException("author", Map.of("id", id.toString()));
                });

        try {
            log.info("Deleting author with id {}.", id);
            authorRepository.delete(author);
        } catch (Exception e) {
            log.error("Error deleting author with id {}.", id);
            throw new BloggingEngineException("author", "error deleting", Map.of("id", id.toString()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Author> pageWithAuthors =authorRepository.findAll(pageCharacteristics);

        if (pageWithAuthors.isEmpty()) {
            throw new NoElementsException(
                    "authors for page number: %s with max %s authors per page".formatted(pageNo, pageSize)
            );
        }

        return createAndReturnAuthorResponse(pageWithAuthors);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAuthors() {
        log.info("Counting all authors.");
        long countingAuthors = authorRepository.count();

        if (countingAuthors <= 0) {
            throw new NoElementsException("authors");
        }

        return countingAuthors;
    }

    @Override
    public Long countAuthorsByFirstName(String firstName) {
        return 0L;
    }

    @Override
    public Long countAuthorsByLastName(String lastName) {
        return 0L;
    }

    @Override
    public Long countHowManyPostsAuthorHas(Long id) {
        return 0L;
    }
}
