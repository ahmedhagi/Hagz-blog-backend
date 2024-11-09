package com.hagz.blog.services;

import com.hagz.blog.model.*;
import com.hagz.blog.payload.request.PostRequest;
import com.hagz.blog.repository.PostRepository;
import com.hagz.blog.repository.TagRepository;
import com.hagz.blog.repository.TopicRepository;
import com.hagz.blog.repository.UserRepository;
import com.hagz.blog.security.services.UserDetailsImpl;
import com.hagz.blog.utils.PostMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    //show all posts
    @Transactional
    public List<Post> showAll() {

        List<Post> posts = postRepository.findAll();
        return posts.stream().collect(Collectors.toList());
    }

    //gets post given a username
    @Transactional
    public List<Post> postByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: Username is not found."));

        String postUsername = user.getUsername();

        List<Post> posts = postRepository.getPostByUsername(postUsername);

        return posts.stream().collect(Collectors.toList());
    }



    //gets post by tag given tag string
    @Transactional
    public List<Post> postsByTag(String name) {

        Tag newTag =  tagRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Error: tag is not found."));

        Long tagID= newTag.getId();


        List<Post> posts = postRepository.getPostByTag(tagID);
        return posts.stream().collect(Collectors.toList());
    }

    @Transactional
    public List<Post> postsByTopic(String name) {

        Topic newTopic =  topicRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Error: topic " + name +  " is not found."));

        Long topicID= newTopic.getId();

        List<Post> posts = postRepository.getPostByTopic(topicID);
        return posts.stream().collect(Collectors.toList());
    }

    //create a post
    @Transactional
    public Post createPost(PostRequest postRequest) {
        Post post = new Post();

        //sets Username
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal();
            post.setUsername(userDetails.getUsername());
        }
        catch(Exception e){
            throw new IllegalArgumentException("User not login");
        }

        postMapper.postUserFromRequest(postRequest,post);

        //sets slug after title is mapped to post
        post.setSlug(post.getTitle().replace(" ","-").toLowerCase());

        //sets time create
        post.setCreatedOn(Instant.now());
        post.setUpdatedOn(Instant.now());

        postRepository.save(post);
        Hibernate.initialize(post.getTopic());
        return post;
    }

    //gets a post given a post id
    @Transactional
    public Post getPost(long ID) {
        Post post = postRepository.findById(ID)
                .orElseThrow(() -> new RuntimeException("Error: post is not found."));
        return post;
    }

    //Update Post
    @Transactional
    public Post updatePost(long ID, PostRequest postRequest ){

        Post post = postRepository.findById(ID).orElseThrow(() -> new RuntimeException("Error: Post is not found."));
        postMapper.postUserFromRequest(postRequest,post);

        //Updates other variables
        post.setUpdatedOn(Instant.now());
        post.setSlug(post.getTitle().replace(" ","-").toLowerCase());

        postRepository.save(post);
        Hibernate.initialize(post.getComments());


        return post;
    }

    //Delete Post
    @Transactional
    public Post deletePost(long ID){
       Post post = getPost(ID);
       postRepository.delete(post);
        Hibernate.initialize(post.getTags());
        Hibernate.initialize(post.getComments());
       return post;
    }

}
