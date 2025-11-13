package com.blogapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Integer postId;

    private String title;

    private String content;

    private String imageName;

    private Date createAt;

    private Date updateAt = new Date();

    private CategoryDto category;

    private UserDto user;

    private Set<CommentDto> comments = new HashSet<>();
}
