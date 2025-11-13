package com.blogapi.services.impl;

import com.blogapi.config.AppConstants;
import com.blogapi.dto.CustomPageResponse;
import com.blogapi.dto.PostDto;
import com.blogapi.entities.Category;
import com.blogapi.entities.Post;
import com.blogapi.entities.User;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.CategoryRepository;
import com.blogapi.repositories.PostRepository;
import com.blogapi.repositories.UserRepository;
import com.blogapi.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {


    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User getUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        Category getCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));


        Post mappedPost = modelMapper.map(postDto, Post.class);
        mappedPost.setUser(getUser);
        mappedPost.setCategory(getCategory);
        mappedPost.setImageName("default.png");
        mappedPost.setCreateAt(new Date());


        Post postCreated = postRepository.save(mappedPost);
        return modelMapper.map(postCreated, PostDto.class);
    }

    @Override
    public PostDto updatePost(Integer postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(postDto.getUpdateAt());
        post.setImageName(postDto.getImageName());
        Post updatedPost = postRepository.save(post);
        return modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void delete(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        postRepository.delete(post);

    }

    @Override
    public PostDto singlePost(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public CustomPageResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortBy.toLowerCase().trim().equals(AppConstants.SORT_BY) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> postPage = postRepository.findAll(pageable);
        List<Post> postList = postPage.getContent();

        List<PostDto> postDtoList = postList.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();

        CustomPageResponse customPageResponse = new CustomPageResponse();
        customPageResponse.setContent(postDtoList);
        customPageResponse.setPageNumber(postPage.getNumber());
        customPageResponse.setPageSize(postPage.getSize());
        customPageResponse.setTotalElements(postPage.getTotalElements());
        customPageResponse.setTotalPages(postPage.getTotalPages());
        customPageResponse.setLastPage(postPage.isLast());


        return customPageResponse;
    }

    @Override
    public List<PostDto> getPostByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
        List<Post> post = postRepository.findByCategory(category);
        return post.stream().map(postDto -> modelMapper.map(postDto, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> getPostByUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        List<Post> posts = postRepository.findByUser(user);
        return posts.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> searchPost(String keyword) {
        List<Post> byTitleContaining = postRepository.findByTitleContaining(keyword);
        return byTitleContaining.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();
    }
}
