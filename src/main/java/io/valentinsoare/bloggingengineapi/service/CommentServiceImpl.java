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
import java.util.Objects;

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

    private Comment getComment(Long commentId, Long postId) {
        return commentRepository.findById(commentId)
                .filter(c -> Objects.equals(c.getPost().getId(), postId))
                .orElseThrow(() -> new ResourceNotFoundException("comment",
                        Map.of(
                            "postId", String.valueOf(postId),
                            "commentId", String.valueOf(commentId)
                        )
                ));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post",
                        new HashMap<>(Map.of("id", String.valueOf(postId))))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse getAllCommentsByPostId(Long postId, int pageNo, int pageSize, String sortBy, String sortDir) {
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

        return CommentResponse.builder()
                .comments(allCommentsAsDTOs)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalCommentsOnPage(pageWithComments.getTotalElements())
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
        Comment newComment = mapToEntity(commentDto);

        Post postById = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post",
                        new HashMap<>(Map.of("id", String.valueOf(postId)))));

        newComment.setPost(postById);

        try {
            Comment saveComment = commentRepository.save(newComment);
            return mapToDTO(saveComment);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public CommentDto updateCommentByIdAndPostId(Long commentId, Long postId, CommentDto commentDto) {
        Comment commentFound = getComment(commentId, postId);

        commentFound.setName(commentDto.getName())
                .setEmail(commentDto.getEmail())
                .setBody(commentDto.getBody())
                .setPost(commentFound.getPost());

        Comment savedComment;

        try {
            savedComment = commentRepository.save(commentFound);
        } catch (Exception e) {
            throw new ResourceViolationException(e.getLocalizedMessage());
        }

        return mapToDTO(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentByIdAndPostId(Long commentId, Long postId) {
        Comment commentFound = getComment(commentId, postId);

        commentFound.getPost().removeComment(commentFound);
        commentRepository.delete(commentFound);
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
