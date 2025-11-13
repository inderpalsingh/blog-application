package com.blogapi.services;

import com.blogapi.dto.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, Integer postId);
    void deleteComment(Integer commentId);
}
