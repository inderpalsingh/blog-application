package com.blogapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;


    private String categoryName;

    @Column(name = "category_desc")
    private String categoryDescription;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();
}
