package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.AuthorDto;
import io.valentinsoare.bloggingengineapi.dto.CategoryDto;
import io.valentinsoare.bloggingengineapi.dto.CommentDto;
import io.valentinsoare.bloggingengineapi.entity.Author;
import io.valentinsoare.bloggingengineapi.entity.Category;
import io.valentinsoare.bloggingengineapi.entity.Comment;
import io.valentinsoare.bloggingengineapi.entity.Post;
import io.valentinsoare.bloggingengineapi.exception.NoElementsException;
import io.valentinsoare.bloggingengineapi.exception.ResourceNotFoundException;
import io.valentinsoare.bloggingengineapi.exception.ResourceViolationException;
import io.valentinsoare.bloggingengineapi.dto.PostDto;
import io.valentinsoare.bloggingengineapi.repository.AuthorRepository;
import io.valentinsoare.bloggingengineapi.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public PostServiceImpl(PostRepository postRepository,
                           ModelMapper modelMapper,
                           CategoryRepository categoryRepository,
                           AuthorRepository authorRepository) {
        this.modelmapper = modelMapper;
        this.postRepository = postRepository;
        this.auxiliaryMethods = AuxiliaryMethods.getInstance();
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    private PostDto mapToDTO(Post post) {
        return modelmapper.map(post, PostDto.class);
    }

    private Post mapToEntity(PostDto postDto) {
        return modelmapper.map(postDto, Post.class);
    }

    private PostResponse preparePostResponseToBeReturned(Page<Post> pageWithPosts) {
        List<PostDto> content = pageWithPosts.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        return PostResponse.builder()
                .pageContent(content)
                .pageNo(pageWithPosts.getNumber())
                .pageSize(pageWithPosts.getSize())
                .totalPostsOnPage(content.size())
                .totalPages(pageWithPosts.getTotalPages())
                .isLast(pageWithPosts.isLast())
                .build();
    }

    @Override
    @Transactional
    public PostDto createPost(PostDto postDto) {
        Set<CategoryDto> categoriesDto = postDto.getCategories();
        Set<AuthorDto> authorsDto = postDto.getAuthors();

        Post newPost = mapToEntity(postDto)
                .setCategories(new HashSet<>())
                .setAuthors(new HashSet<>());

        for (CategoryDto category : categoriesDto) {
            Optional<Category> categoryByName = categoryRepository.findCategoryByName(category.getName());

            if (categoryByName.isPresent()) {
                Category foundCategory = categoryByName.get();
                newPost.addCategory(foundCategory);
            }
        }

        if (newPost.getCategories().isEmpty()) {
            throw new ResourceViolationException("No categories found for post.");
        }

        for (AuthorDto author : authorsDto) {
            Optional<Author> authorByEmail = authorRepository.findByEmail(author.getEmail());

            if (authorByEmail.isPresent()) {
                Author foundAuthor = authorByEmail.get();
                newPost.addAuthor(foundAuthor);
            }
        }

        if (newPost.getAuthors().isEmpty()) {
            throw new ResourceViolationException("No authors found for post.");
        }

        try {
            Post savedPost = postRepository.save(newPost);
            return mapToDTO(savedPost);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> pageWithPosts = postRepository.findAll(pageCharacteristics);

        if (pageWithPosts.isEmpty()) {
            throw new NoElementsException(
                    "posts for page number: %s with max %s posts per page".formatted(pageNo, pageSize)
            );
        }

        return preparePostResponseToBeReturned(pageWithPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("post", new HashMap<>(Map.of("id", String.valueOf(id))))
                );

        return mapToDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostByTitle(String title) {
        Post post = postRepository.getPostByTitle(title);

        if (post == null || post.getId() < 1) {
            throw new ResourceNotFoundException("post", new HashMap<>(Map.of("title", title)));
        }

        return mapToDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostsByAuthorEmail(String email, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> pageWithPosts = postRepository.getAllPostsByAuthorEmail(email, pageCharacteristics);

        if (pageWithPosts.isEmpty()) {
            throw new NoElementsException(String.format("posts by author email: %s", email));
        }

        return preparePostResponseToBeReturned(pageWithPosts);
    }

    @Override
    @Transactional
    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("post", new HashMap<>(Map.of("id", String.valueOf(id))))
                );

        post.setTitle(postDto.getTitle())
                .setDescription(postDto.getDescription())
                .setContent(postDto.getContent())
                .setCategories(new HashSet<>())
                .setAuthors(new HashSet<>())
                .setComments(new HashSet<>());

        postDto.getCategories().forEach(categoryDto -> {
            Optional<Category> categoryByName = categoryRepository.findCategoryByName(categoryDto.getName());

            if (categoryByName.isPresent()) {
                Category foundCategory = categoryByName.get();
                post.addCategory(foundCategory);
            }
        });

        postDto.getAuthors().forEach(authorDto -> {
            Optional<Author> authorByEmail = authorRepository.findByEmail(authorDto.getEmail());

            if (authorByEmail.isPresent()) {
                Author foundAuthor = authorByEmail.get();
                post.addAuthor(foundAuthor);
            }
        });

        Set<CommentDto> commentsDto = postDto.getComments();

        if (!commentsDto.isEmpty()) {
            commentsDto.forEach(commentDto -> {
                Comment comment = modelmapper.map(commentDto, Comment.class);
                post.addComment(comment);
            });
        }

        Post updatedPost;

        try {
            updatedPost = postRepository.save(post);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }

        return mapToDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", new HashMap<>(Map.of("id", String.valueOf(id)))));

        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllPosts() {
        Long count = postRepository.count();

        if (count < 1) {
            throw new NoElementsException("posts");
        }

        return count;
    }

    @Override
    @Transactional
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPostByAuthorEmail(@NotNull String email) {
        Long count = postRepository.countPostByAuthorEmail(email);

        if (count < 1) {
            throw new NoElementsException(String.format("posts by author email: %s", email));
        }

        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPostsByAuthorId(Long authorId) {
        Long c = postRepository.countPostByAuthorId(authorId);

        if (c < 1) {
            throw new NoElementsException("posts by author with id: %s".formatted(authorId));
        }

        return c;
    }

    @Override
    @Transactional
    public void deleteAllPostsByAuthorId(Long authorId) {
        postRepository.deleteAllByAuthorId(authorId);
    }

    @Override
    @Transactional
    public void deletePostByAuthorIdAndPostId(Long authorId, Long postId) {
        postRepository.deletePostByAuthorIdAndPostId(authorId, postId);
    }

    @Override
    @Transactional
    public void deleteAllPostsByAuthorEmail(String email) {
        postRepository.deleteAllByAuthorEmail(email);
    }

    @Override
    @Transactional
    public void deleteAllPostsByAuthorFirstNameAndLastName(String firstName, String lastName) {
        postRepository.deleteAllPostsByAuthorFirstNameAndLastName(firstName, lastName);
    }

    @Override
    @Transactional
    public void deleteAllPostsByAuthorLastName(String lastName) {
        postRepository.deleteAllPostsByAuthorLastName(lastName);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostsByCategoryName(String categoryName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> pageWithPosts = postRepository.getAllPostsByCategoryName(categoryName, pageCharacteristics);

        if (pageWithPosts.isEmpty()) {
            throw new NoElementsException(String.format("posts by category name: %s", categoryName));
        }

        return preparePostResponseToBeReturned(pageWithPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostsByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> pageWithPosts = postRepository.getAllPostsByCategoryId(categoryId, pageCharacteristics);

        if (pageWithPosts.isEmpty()) {
            throw new NoElementsException(String.format("posts by category id: %s", categoryId));
        }

        return preparePostResponseToBeReturned(pageWithPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPostsByCategoryName(String categoryName) {
        Long l = postRepository.countPostsByCategoryName(categoryName);

        if (l < 1) {
            throw new NoElementsException("posts by category name: %s".formatted(categoryName));
        }

        return l;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPostByCategoryId(Long categoryId) {
        Long l = postRepository.countPostByCategoryId(categoryId);

        if (l < 1) {
            throw new NoElementsException("posts by category id: %s".formatted(categoryId));
        }

        return l;
    }

    @Override
    @Transactional
    public void deleteAllPostsByCategoryId(Long categoryId) {
        postRepository.deleteAllPostsByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public void deletePostByCategoryIdAndPostId(Long categoryId, Long postId) {
        postRepository.deletePostByCategoryIdAndPostId(categoryId, postId);
    }

    @Override
    public void deleteAllPostsByCategoryName(String categoryName) {

    }

    @Override
    public PostResponse getPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public PostResponse getPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public PostResponse getPostsByAuthorEmailAndCategoryName(String email, String categoryName, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public PostResponse getPostsByAuthorIdAndCategoryId(Long authorId, Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public Long countPostByAuthorLastNameAndCategoryName(String lastName, String categoryName) {
        return 0L;
    }

    @Override
    public Long countPostByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName) {
        return 0L;
    }

    @Override
    public Long countPostByAuthorEmailAndCategoryName(String email, String categoryName) {
        return 0L;
    }

    @Override
    public Long countPostByAuthorIdAndCategoryId(Long authorId, Long categoryId) {
        return 0L;
    }

    @Override
    public void deleteAllPostsByAuthorLastNameAndCategoryName(String lastName, String categoryName) {

    }

    @Override
    public void deleteAllPostsByAuthorFirstNameAndLastNameAndCategoryName(String firstName, String lastName, String categoryName) {

    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostsByAuthorId(Long authorId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> pageWithPosts = postRepository.getAllPostsByAuthorId(authorId, pageCharacteristics);

        if (pageWithPosts.getContent().isEmpty()) {
            throw new NoElementsException("posts by author with id: %s".formatted(authorId));
        }

        return preparePostResponseToBeReturned(pageWithPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostsByAuthorLastName(String lastName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> allPostsByAuthorLastName = postRepository.getAllPostsByAuthorLastName(lastName, pageCharacteristics);

        if (allPostsByAuthorLastName.isEmpty()) {
            throw new NoElementsException("posts by author with last name: %s".formatted(lastName));
        }

        return preparePostResponseToBeReturned(allPostsByAuthorLastName);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostsByAuthorFirstNameAndLastName(String firstName, String lastName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageCharacteristics = auxiliaryMethods.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Post> allPostsByAuthorFirstNameAndLastName = postRepository.getAllPostsByAuthorFirstNameAndLastName(firstName, lastName, pageCharacteristics);

        if (allPostsByAuthorFirstNameAndLastName.isEmpty()) {
            throw new NoElementsException("posts by author with firstname: %s last name: %s".formatted(firstName, lastName));
        }

        return preparePostResponseToBeReturned(allPostsByAuthorFirstNameAndLastName);
    }
}
