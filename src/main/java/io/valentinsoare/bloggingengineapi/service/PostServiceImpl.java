package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.entity.Author;
import io.valentinsoare.bloggingengineapi.entity.Post;
import io.valentinsoare.bloggingengineapi.exception.NoElementsException;
import io.valentinsoare.bloggingengineapi.exception.ResourceNotFoundException;
import io.valentinsoare.bloggingengineapi.exception.ResourceViolationException;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.response.PostResponse;
import io.valentinsoare.bloggingengineapi.repository.PostRepository;
import io.valentinsoare.bloggingengineapi.utilities.AuxiliaryMethods;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final AuxiliaryMethods auxiliaryMethods;
    private final ModelMapper modelmapper;

    public PostServiceImpl(PostRepository postRepository,
                           ModelMapper modelMapper) {
        this.modelmapper = modelMapper;
        this.postRepository = postRepository;
        this.auxiliaryMethods = AuxiliaryMethods.getInstance();
    }

    private PostDto mapToDTO(Post post) {
        return modelmapper.map(post, PostDto.class);
    }

    private Post mapToEntity(PostDto postDto) {
        return modelmapper.map(postDto, Post.class);
    }

    @Override
    @Transactional
    public PostDto createPost(@NotNull PostDto postDto) {
        Post newPost = mapToEntity(postDto);

        Post savedPost;
        log.info("Creating post with title: {}.", postDto.getTitle());

        try {
            savedPost = postRepository.save(newPost);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }

        log.info("Post with title {} created.", postDto.getTitle());
        return mapToDTO(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getAllPosts(int pageNo, int pageSize, @NotNull String sortBy, @NotNull String sortDir) {
        log.info("Getting all posts from page number: {} and page size: {} in sorted order.", pageNo, pageSize);

        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> pageWithPosts = postRepository.findAll(pageCharacteristics);

        List<PostDto> content = pageWithPosts.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        if (content.isEmpty()) {
            throw new NoElementsException("posts for page number: %s with max %s posts per page".formatted(pageNo, pageSize));
        }

        return PostResponse.builder()
                .pageContent(content)
                .pageNo(pageCharacteristics.getPageNumber())
                .pageSize(pageCharacteristics.getPageSize())
                .totalPostsOnPage(content.size())
                .totalPages(pageWithPosts.getTotalPages())
                .isLast(pageWithPosts.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(long id) {
        log.info("Getting post with id: {}.", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", new HashMap<>(Map.of("id", String.valueOf(id)))));

        return mapToDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostByTitle(@NotNull String title) {
        log.info("Getting post with title: {}", title);

        Post post = postRepository.getPostByTitle(title);

        if (post == null || post.getId() < 1) {
            throw new ResourceNotFoundException("post", new HashMap<>(Map.of("title", title)));
        }

        return mapToDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByAuthorEmail(@NotNull String email) {
        log.info("Getting all posts by author: {}", email);

        return postRepository.getAllByAuthorEmail(email).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public PostDto updatePost(long id, @NotNull  @NotNull PostDto postDto) {
        log.info("Updating post with id: {}.", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("post", new HashMap<>(Map.of("id", String.valueOf(id))))
                );

        post.setTitle(auxiliaryMethods.updateIfPresent(postDto.getTitle(), post.getTitle()))
                .setAuthors((Set<Author>) auxiliaryMethods.updateIfPresent(postDto.getAuthor(), post.getAuthors()))
                .setDescription(auxiliaryMethods.updateIfPresent(postDto.getDescription(), post.getDescription()))
                .setContent(auxiliaryMethods.updateIfPresent(postDto.getContent(), post.getContent()));

        Post updatedPost;
        try {
            updatedPost = postRepository.save(post);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }

        log.info("Post with id: {} updated.", id);
        return mapToDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(long id) {
        log.info("Deleting post with id: {}.", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", new HashMap<>(Map.of("id", String.valueOf(id)))));

        postRepository.delete(post);
        log.info("Post with id: {} deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllPosts() {
        log.info("Counting all posts.");

        long count = postRepository.count();

        if (count < 1) {
            throw new NoElementsException("posts");
        }

        return count;
    }

    @Override
    @Transactional
    public void deleteAllPosts() {
        log.info("Deleting all posts.");

        postRepository.deleteAll();
        log.info("All posts deleted.");
    }

    @Override
    @Transactional(readOnly = true)
    public long countPostByAuthorEmail(@NotNull String email) {
        log.info("Counting posts by author email: {}.", email);

        long count = postRepository.countPostByAuthorEmail(email);

        if (count < 1) {
            throw new NoElementsException(String.format("posts by author email: %s", email));
        }

        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByAuthorId(long id) {
        log.info("Fetching all posts by author with id: {}.", id);

        List<PostDto> postsFromDb = postRepository.getAllByAuthorId(id).stream()
                .map(this::mapToDTO)
                .toList();

        if (postsFromDb.isEmpty()) {
            throw new NoElementsException("posts by author with id: %s".formatted(id));
        }

        log.info("Found {} posts by author with id: {}.", postsFromDb.size(), id);
        return postsFromDb;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByAuthorLastName(@NotNull String lastName) {
        log.info("Fetching all posts by author with last name: {}.", lastName);

        List<PostDto> fetchedPosts = postRepository.getAllByAuthorLastName(lastName).stream()
                .map(this::mapToDTO)
                .toList();

        if (fetchedPosts.isEmpty()) {
            throw new NoElementsException("posts by author with last name: %s".formatted(lastName));
        }

        log.info("Found {} posts by author with last name: {}.", fetchedPosts.size(), lastName);
        return fetchedPosts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByAuthorsEmail(@NotNull List<String> authorsEmail) {
        log.info("Fetching all posts by authors with emails: {}.", authorsEmail);

        List<PostDto> postsFromDb = postRepository.getAllByAuthorsEmail(authorsEmail).stream()
                .map(this::mapToDTO)
                .toList();

        if (postsFromDb.isEmpty()) {
            throw new NoElementsException("posts by authors with emails: %s".formatted(authorsEmail));
        }

        log.info("Found {} posts by authors with emails: {}.", postsFromDb.size(), authorsEmail);
        return postsFromDb;
    }
}
