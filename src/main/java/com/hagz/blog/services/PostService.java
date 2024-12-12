package com.hagz.blog.services;

import com.hagz.blog.model.*;
import com.hagz.blog.payload.request.PostBodyRequest;
import com.hagz.blog.repository.PostRepository;
import com.hagz.blog.repository.TagRepository;
import com.hagz.blog.repository.TopicRepository;
import com.hagz.blog.repository.UserRepository;
import com.hagz.blog.security.services.UserDetailsImpl;
import com.hagz.blog.utils.PostMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    //Get Post with Pagination
    @Transactional
    public Page<Post> getPostWithPagination(int offset, int pageSize) {
        return postRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC, "createdOn")));
    }

    //gets post given a username
    @Transactional
    public Page<Post> postByUsername(String username , int offset, int pageSize) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: Username is not found."));

        String postUsername = user.getUsername();

        Page<Post> posts = postRepository.getPostByUsername(postUsername,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"createdOn")));

        return posts;
    }



    //gets post by tag given tag string
    @Transactional
    public Page<Post> postsByTag(String name, int offset, int pageSize) {

        Tag newTag =  tagRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Error: tag is not found."));

        Long tagID= newTag.getId();


        Page<Post> posts = postRepository.getPostByTag(tagID,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"createdOn")));
        return posts;
    }

    @Transactional
    public Page<Post> postsByTopic(String name , int offset, int pageSize) {

        Topic newTopic =  topicRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Error: topic " + name +  " is not found."));

        Long topicID= newTopic.getId();
        Page<Post> posts = postRepository.getPostByTopic(topicID,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"createdOn")));
        return posts;
    }


    //create a post
    @Transactional
    public Post createPost(PostBodyRequest postBodyRequest) {
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

       //map postBodyRequest to post
        postMapper.postUserFromRequest(postBodyRequest,post);


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

    //Get all Post based on a certain field
    @Transactional
    public List<Post> sortBasedUponSomeField(String field) {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }


    //Update Post
    @Transactional
    public Post updatePost(long ID, PostBodyRequest postBodyRequest ){

        Post post = postRepository.findById(ID).orElseThrow(() -> new RuntimeException("Error: Post is not found."));

        //Maps fields to post
        postMapper.postUserFromRequest(postBodyRequest,post);

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
