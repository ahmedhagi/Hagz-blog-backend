package com.hagz.blog.repository;

import com.hagz.blog.model.Post;
import java.util.List;

import com.hagz.blog.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {


    @Query(value = "from Post p where p.topic.id = :topicID")
    Page<Post> getPostByTopic(@Param("topicID") Long topicID, Pageable pageable);

    @Query(value = "select p from Post p join p.tags ptg where ptg.id = :tagID")
    Page<Post> getPostByTag(@Param("tagID") Long tagID , Pageable pageable);

   @Query(value = "from Post p WHERE p.username = :username")
   Page<Post> getPostByUsername(@Param("username") String username, Pageable pageable);


}
