package com.blogapi.services.impl;

import com.blogapi.dto.CommentDto;
import com.blogapi.entities.Comment;
import com.blogapi.entities.Post;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.CommentRepository;
import com.blogapi.repositories.PostRepository;
import com.blogapi.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;


    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        return modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", commentId));
        commentRepository.save(comment);

    }
}
