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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * Find related posts based on shared tags with fallback mechanisms.
     * Uses MapStruct mapper to convert entities to DTOs.
     *
     * Priority order:
     * 1. Posts sharing the most tags with the given post
     * 2. Posts in the same topic (if limit not reached)
     * 3. Recent posts (if limit still not reached)
     */
    @Transactional(readOnly = true)
    public List<PostDto> findRelatedPosts(Long postId, int limit) {
        Set<Long> collectedIds = new LinkedHashSet<>();
        List<Long> excludeIds = new ArrayList<>();
        excludeIds.add(postId);

        // Collect related posts by priority: tags -> topic -> recent
        collectTagRelatedPosts(postId, limit, collectedIds, excludeIds);
        collectFallbackPosts(postId, limit, collectedIds, excludeIds, this::fetchTopicRelatedIds);
        collectFallbackPosts(postId, limit, collectedIds, excludeIds, this::fetchRecentIds);

        if (collectedIds.isEmpty()) {
            return List.of();
        }

        List<Post> sortedPosts = fetchAndSortPosts(collectedIds);
        return postResponseMapper.postsToPostDtos(sortedPosts);
    }

    /**
     * Collect post IDs related by shared tags.
     * Primary method for finding related content.
     */
    private void collectTagRelatedPosts(Long postId,
                                        int limit,
                                        Set<Long> collectedIds,
                                        List<Long> excludeIds) {
        List<Long> tagRelatedIds = postRepository.findRelatedPostIdsByTags(
                postId,
                PageRequest.of(0, limit)
        );
        collectedIds.addAll(tagRelatedIds);
        excludeIds.addAll(tagRelatedIds);
    }

    /**
     * Collect fallback post IDs using the provided fetcher function.
     * Only executes if the current collection hasn't reached the limit.
     */
    private void collectFallbackPosts(Long postId,
                                      int limit,
                                      Set<Long> collectedIds,
                                      List<Long> excludeIds,
                                      FallbackIdFetcher fetcher) {
        if (collectedIds.size() >= limit) {
            return;
        }

        int remaining = limit - collectedIds.size();
        List<Long> fallbackIds = fetcher.fetch(postId, excludeIds, remaining);
        collectedIds.addAll(fallbackIds);
        excludeIds.addAll(fallbackIds);
    }

    /**
     * Fetch topic-related post IDs excluding already collected posts.
     */
    private List<Long> fetchTopicRelatedIds(Long postId, List<Long> excludeIds, int limit) {
        return postRepository.findRelatedPostIdsByTopicExcluding(
                postId,
                excludeIds,
                PageRequest.of(0, limit)
        );
    }

    /**
     * Fetch recent post IDs excluding already collected posts.
     */
    private List<Long> fetchRecentIds(Long postId, List<Long> excludeIds, int limit) {
        return postRepository.findRecentPostIdsExcluding(
                excludeIds,
                PageRequest.of(0, limit)
        );
    }

    /**
     * Fetch full post entities and sort them by collection order.
     * Removes duplicates caused by JOIN FETCH while preserving order.
     */
    private List<Post> fetchAndSortPosts(Set<Long> collectedIds) {
        List<Long> orderedIds = new ArrayList<>(collectedIds);
        List<Post> posts = postRepository.findPostsWithAssociationsByIds(orderedIds);

        // Remove duplicates caused by JOIN FETCH
        List<Post> uniquePosts = new ArrayList<>(new LinkedHashSet<>(posts));

        // Sort by original collection order
        Map<Long, Integer> orderMap = IntStream.range(0, orderedIds.size())
                .boxed()
                .collect(Collectors.toMap(orderedIds::get, i -> i));

        uniquePosts.sort(Comparator.comparing(p -> orderMap.getOrDefault(p.getId(), Integer.MAX_VALUE)));

        return uniquePosts;
    }

    /**
     * Functional interface for fallback ID fetching strategies.
     * Enables reusable collection logic across different fallback methods.
     */
    @FunctionalInterface
    private interface FallbackIdFetcher {
        List<Long> fetch(Long postId, List<Long> excludeIds, int limit);
    }


}
