package com.blogapi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Integer postId;

    private String title;

    private String content;

    private String imageUrl;

    private LocalDateTime createAt = LocalDateTime.now();

    private LocalDateTime updateAt= LocalDateTime.now();

    private CategoryDto category;

    private UserDto user;

    private Set<CommentDto> comments = new HashSet<>();
}
