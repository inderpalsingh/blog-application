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
import com.blogapi.services.FileStorageService;
import com.blogapi.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Value("${app.base-url}")
    private String baseUrl;

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private FileStorageService fileStorageService;

    public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, UserRepository userRepository, ModelMapper modelMapper, FileStorageService fileStorageService) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.fileStorageService = fileStorageService;
    }

    // This method converts the filename to full URL
    public PostDto createBaseUrl(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);

        // Convert image filename to full URL
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            String imageUrl = baseUrl + "/images/" + post.getImageUrl();
            postDto.setImageUrl(imageUrl);
            System.out.println("imageUrl: " + imageUrl);
        } else {
            postDto.setImageUrl(null);
        }

        return postDto;
    }

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId, MultipartFile imageFile) {
        // Find user and category
        User getUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        Category getCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

        Post mappedPost = modelMapper.map(postDto, Post.class);
        mappedPost.setUser(getUser);
        mappedPost.setCategory(getCategory);
        mappedPost.setCreateAt(LocalDateTime.now());

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String storedFile = fileStorageService.storeFile(imageFile);
            mappedPost.setImageUrl(storedFile);
        }


        Post postCreated = postRepository.save(mappedPost);
        return createBaseUrl(postCreated);
    }

    @Override
    public PostDto updatePost(Integer postId, PostDto postDto, MultipartFile imageFile) {
        Post existingPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));

        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (existingPost.getImageUrl() != null) {
                try {
                    fileStorageService.deleteFile(existingPost.getImageUrl());
                } catch (Exception e) {
                    log.warn("Failed to delete old image: {}", existingPost.getImageUrl(), e);
                                    }
            }
            // Upload new image
            String newImageUrl = fileStorageService.storeFile(imageFile);
            existingPost.setImageUrl(newImageUrl);
        } else {
            // Preserve existing image URL
            log.debug("No new image provided, preserving existing image: {}", existingPost.getImageUrl());
        }
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(existingPost);
        log.info("✅ UPDATE COMPLETE - Post {} image: {}", postId, updatedPost.getImageUrl());
        return modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void delete(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));

        // Delete associated image
        if (post.getImageUrl() != null) {
            fileStorageService.deleteFile(post.getImageUrl());
        }
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

        List<PostDto> postDtoList = postList.stream().map(this::createBaseUrl).toList();

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

    @Override
    public PostDto updatePostImage(Integer id, MultipartFile imageFile) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", id));

        if (imageFile != null && !imageFile.isEmpty()) {

            // Delete old image if exists
            if (existingPost.getImageUrl() != null) {
                fileStorageService.deleteFile(existingPost.getImageUrl());
            }
            String fileName = fileStorageService.storeFile(imageFile);
            existingPost.setImageUrl(fileName);
        }
        Post saved = postRepository.save(existingPost);
        return modelMapper.map(saved, PostDto.class);
    }
}
