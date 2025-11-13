package com.hagz.blog.services;

import com.hagz.blog.model.*;
import com.hagz.blog.payload.request.PostBodyRequest;
import com.hagz.blog.payload.response.PostCardDTO;
import com.hagz.blog.payload.response.PostDto;
import com.hagz.blog.repository.PostRepository;
import com.hagz.blog.repository.TagRepository;
import com.hagz.blog.repository.TopicRepository;
import com.hagz.blog.repository.UserRepository;
import com.hagz.blog.security.services.UserDetailsImpl;
import com.hagz.blog.utils.PostMapper;
import com.hagz.blog.utils.PostResponseMapper;
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

    @Autowired
    private PostResponseMapper postResponseMapper;

    //show all posts
    @Transactional
    public List<Post> showAll() {

        List<Post> posts = postRepository.findAll();
        return posts.stream().collect(Collectors.toList());
    }

    //Get Post with Pagination
    @Transactional(readOnly = true)
    public Page<PostCardDTO> getPostCardsWithPagination(int offset, int pageSize) {
        return postRepository.findPostCards(
                PageRequest.of(offset, pageSize)
        );
    }

    //Get Post with Filters
    public Page<PostCardDTO> getPostCardsWithFilters(
            String username, String topicName, String tagName,
            int offset, int pageSize) {
        return postRepository.findPostCardsWithFilters(
                username, topicName, tagName,
                PageRequest.of(offset, pageSize)
        );
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
                .orElseThrow(() -> new RuntimeException("Error: post " + ID + " is not found."));
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



    /**
     * Find related posts based on shared tags.
     * Uses MapStruct mapper to convert entities to DTOs.
     */
    @Transactional(readOnly = true)
    public List<PostDto> findRelatedPosts(Long postId, int limit) {
        // Step 1: Get related post IDs sorted by relevance
        List<Long> postIds = postRepository.findRelatedPostIdsByTags(
                postId,
                PageRequest.of(0, limit)
        );

        // Step 2: Fetch full posts with all associations if there are results
        if (postIds.isEmpty()) {
            return List.of();
        }

        List<Post> relatedPosts = postRepository.findPostsWithAssociationsByIds(postIds);

        // Maintain the order from step 1
        relatedPosts.sort((a, b) -> {
            int indexA = postIds.indexOf(a.getId());
            int indexB = postIds.indexOf(b.getId());
            return Integer.compare(indexA, indexB);
        });

        return postResponseMapper.postsToPostDtos(relatedPosts);
    }

}
