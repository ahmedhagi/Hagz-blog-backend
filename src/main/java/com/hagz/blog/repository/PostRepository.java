package com.hagz.blog.repository;

import com.hagz.blog.model.Post;
import java.util.List;

import com.hagz.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {


    @Query(value = "from Post p join p.topic ptc where ptc.id = :topicID")
    List<Post> getPostByTopic(@Param("topicID") Long topicID);

    @Query(value = "from Post p join p.tags ptg where ptg.id = :tagID")
    List<Post> getPostByTag(@Param("tagID") Long tagID);

   @Query(value = "from Post p WHERE p.username = :username")
   List<Post> getPostByUsername(@Param("username") String username);


}
