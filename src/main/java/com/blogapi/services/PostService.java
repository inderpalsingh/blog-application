package com.blogapi.services;


import com.blogapi.dto.CustomPageResponse;
import com.blogapi.dto.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);


    PostDto updatePost(Integer postId, PostDto postDto);


    void delete(Integer postId);


    PostDto singlePost(Integer postId);


    CustomPageResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);


    List<PostDto> getPostByCategory(Integer categoryId);


    List<PostDto> getPostByUser(Integer userId);


    List<PostDto> searchPost(String keyword);


}
