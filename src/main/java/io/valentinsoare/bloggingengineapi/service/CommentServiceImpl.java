package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.CommentDto;
import io.valentinsoare.bloggingengineapi.entity.Comment;
import io.valentinsoare.bloggingengineapi.entity.Post;
import io.valentinsoare.bloggingengineapi.exception.ResourceNotFoundException;
import io.valentinsoare.bloggingengineapi.exception.ResourceViolationException;
import io.valentinsoare.bloggingengineapi.repository.CommentRepository;
import io.valentinsoare.bloggingengineapi.repository.PostRepository;
import io.valentinsoare.bloggingengineapi.response.CommentResponse;
import io.valentinsoare.bloggingengineapi.utilities.AuxiliaryMethods;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuxiliaryMethods auxiliaryMethodsComment;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.auxiliaryMethodsComment = AuxiliaryMethods.getInstance();
        this.modelMapper = modelMapper;
    }

    private CommentDto mapToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }

    private Comment getComment(long commentId, long postId) {
        return commentRepository.findById(commentId)
                .filter(c -> c.getPost().getId() == postId)
                .orElseThrow(() -> new ResourceNotFoundException("comment",
                        Map.of(
                            "postId", String.valueOf(postId),
                            "commentId", String.valueOf(commentId)
                        )
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse getAllCommentsByPostId(Long postId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Finding all comments from post with id: {}. Page number: {} and page size: {} in sorted order.",
                postId, pageNo, pageSize);

        Pageable pageCharacteristics = auxiliaryMethodsComment.sortingWithDirections(sortDir, sortBy, pageNo, pageSize);

        Page<Comment> pageWithComments = commentRepository.findByPostId(postId, pageCharacteristics);

        List<CommentDto> allCommentsAsDTOs = pageWithComments.stream()
                .map(this::mapToDTO)
                .toList();

        if (allCommentsAsDTOs.isEmpty()) {
            throw new ResourceNotFoundException("comments",
                    new HashMap<>(Map.of("postId", String.valueOf(postId)))
            );
        }

        log.info("Mapping comments to commentDto.");
        return CommentResponse.builder()
                .comments(allCommentsAsDTOs)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalCommentsOnPage((int) pageWithComments.getTotalElements())
                .totalPages(pageWithComments.getTotalPages())
                .isLast(pageWithComments.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentByIdAndPostId(Long commentId, Long postId) {
        Comment comment = getComment(commentId, postId);
        return mapToDTO(comment);
    }

    @Override
    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        log.info("Converting from commentDto for post with id: {}.", postId);
        Comment newComment = mapToEntity(commentDto);

        log.info("Finding post with id: {}.", postId);
        Post postById = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post",
                        new HashMap<>(Map.of("id", String.valueOf(postId)))));

        newComment.setPost(postById);

        try {
            log.info("Saving comment to database.");
            Comment saveComment = commentRepository.save(newComment);
            log.info("Comment saved to database with success.");

            return mapToDTO(saveComment);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public CommentDto updateCommentById(Long commentId, Long postId, CommentDto commentDto) {
        log.info("Updating comment with id: {} for post with id: {}.", commentId, postId);
        Comment commentFound = getComment(commentId, postId);

        log.info("Updating comment fields.");
        commentFound.setName(auxiliaryMethodsComment.updateIfPresent(commentDto.getName(), commentFound.getName()))
                .setEmail(auxiliaryMethodsComment.updateIfPresent(commentDto.getEmail(), commentFound.getEmail()))
                .setBody(auxiliaryMethodsComment.updateIfPresent(commentDto.getBody(), commentFound.getBody()));

        Comment savedComment;

        try {
            log.info("Saving updated comment to database.");
            savedComment = commentRepository.save(commentFound);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }

        log.info("Comment updated with success.");
        return mapToDTO(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId, Long postId) {
        log.info("Finding comment with id: {} for post with id: {}.", commentId, postId);
        Comment commentFound = getComment(commentId, postId);

        log.info("Removing comment from post.");
        commentFound.getPost().removeComment(commentFound);

        log.info("Deleting comment with id: {}.", commentId);
        commentRepository.delete(commentFound);
        log.info("Comment with id: {} deleted.", commentId);
    }

    @Override
    public CommentResponse getAllCommentsByPostTitle(String postTitle, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public Long countAllCommentsByPostId(Long postId) {
        return 0L;
    }

    @Override
    public void deleteAllCommentsByPostId(Long postId) {

    }

    @Override
    public CommentResponse getAllCommentsByEmail(String email, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public Long countAllCommentsByEmail(String email) {
        return 0L;
    }

    @Override
    public void deleteAllCommentsByEmail(String email) {

    }

    @Override
    public CommentResponse getAllCommentsByName(String name, int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public Long countAllCommentsByName(String name) {
        return 0L;
    }

    @Override
    public void deleteAllCommentsByName(String name) {

    }
}
