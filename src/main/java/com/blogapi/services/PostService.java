package com.blogapi.services;


import com.blogapi.dto.CustomPageResponse;
import com.blogapi.dto.PostDto;
import com.blogapi.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {



    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId, MultipartFile file);


    PostDto updatePost(Integer postId, PostDto postDto, MultipartFile file);


    void delete(Integer postId);


    PostDto singlePost(Integer postId);


    CustomPageResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);


    List<PostDto> getPostByCategory(Integer categoryId);


    List<PostDto> getPostByUser(Integer userId);


    List<PostDto> searchPost(String keyword);


    PostDto updatePostImage(Integer id, MultipartFile imageFile);

}
