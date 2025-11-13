package com.blogapi.controllers;

import com.blogapi.config.AppConstants;
import com.blogapi.dto.CustomPageResponse;
import com.blogapi.dto.PostDto;
import com.blogapi.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/user/{userId}/category/{categoryId}/post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId) {
        PostDto post = postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<CustomPageResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {

        CustomPageResponse allPosts = postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getSinglePost(@PathVariable Integer postId) {
        PostDto post = postService.singlePost(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getByCategory(@PathVariable Integer categoryId) {
        List<PostDto> postByCategory = postService.getPostByCategory(categoryId);
        return new ResponseEntity<>(postByCategory, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getByUser(@PathVariable Integer userId) {
        List<PostDto> postByUser = postService.getPostByUser(userId);
        return new ResponseEntity<>(postByUser, HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer postId, @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(postId, postDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<PostDto>> searchPostTitle(@PathVariable String title) {
        List<PostDto> postDtos = postService.searchPost(title);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }


}
