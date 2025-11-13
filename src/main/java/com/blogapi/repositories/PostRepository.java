package com.blogapi.repositories;

import com.blogapi.entities.Category;
import com.blogapi.entities.Post;
import com.blogapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);

    List<Post> findByCategory(Category category);

    List<Post> findByTitleContaining(String title);


}
