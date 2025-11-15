package com.blogapi.controllers;

import com.blogapi.config.AppConstants;
import com.blogapi.dto.CustomPageResponse;
import com.blogapi.dto.PostDto;
import com.blogapi.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("posts/user/{userId}/category/{categoryId}/post")
    public ResponseEntity<PostDto> createPost(
            @RequestPart(value = "post") PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        PostDto post = postService.createPost(postDto, userId, categoryId, imageFile);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }


    @GetMapping("/posts")
    public ResponseEntity<CustomPageResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {

        CustomPageResponse allPosts = postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<PostDto> getSinglePost(@PathVariable Integer postId) {
        PostDto post = postService.singlePost(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("posts/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getByCategory(@PathVariable Integer categoryId) {
        List<PostDto> postByCategory = postService.getPostByCategory(categoryId);
        return new ResponseEntity<>(postByCategory, HttpStatus.OK);
    }

    @GetMapping("posts/user/{userId}")
    public ResponseEntity<List<PostDto>> getByUser(@PathVariable Integer userId) {
        List<PostDto> postByUser = postService.getPostByUser(userId);
        return new ResponseEntity<>(postByUser, HttpStatus.OK);
    }

    @PutMapping("posts/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Integer postId,
            @RequestBody PostDto postDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile

    ) {
        PostDto updatedPost = postService.updatePost(postId, postDto, imageFile);
        return new ResponseEntity<>(updatedPost, HttpStatus.CREATED);
    }

    @DeleteMapping("posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("posts/search/{title}")
    public ResponseEntity<List<PostDto>> searchPostTitle(@PathVariable String title) {
        List<PostDto> postDtos = postService.searchPost(title);
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("posts/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId) {
        List<PostDto> postByUser = postService.getPostByUser(userId);
        return ResponseEntity.ok(postByUser);
    }

    @GetMapping("posts/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer categoryId) {
        List<PostDto> postByCategory = postService.getPostByCategory(categoryId);
        return ResponseEntity.ok(postByCategory);
    }

    @PatchMapping("posts/{id}/image")
    public ResponseEntity<PostDto> updatePostImage(
            @PathVariable Integer id,
            @RequestPart("image") MultipartFile imageFile
    ) {
        PostDto updatePostImage = postService.updatePostImage(id, imageFile);
        return ResponseEntity.ok(updatePostImage);
    }


}
