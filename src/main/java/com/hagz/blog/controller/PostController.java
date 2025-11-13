package com.hagz.blog.controller;

import com.hagz.blog.model.Post;
import com.hagz.blog.payload.request.PostBodyRequest;
import com.hagz.blog.payload.response.PostCardDTO;
import com.hagz.blog.payload.response.PostDto;
import com.hagz.blog.services.CommentService;
import com.hagz.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    //shows all posts
    @GetMapping("/all")
    public ResponseEntity<List<Post>> showAllPosts() {
        return new ResponseEntity<>(postService.showAll(), HttpStatus.OK);
    }


    @GetMapping("/get/pagination/{offset}/{pageSize}/")
    @ResponseBody
    public ResponseEntity<Page<PostCardDTO>> getPostCardsWithPagination(
            @PathVariable int offset,
            @PathVariable int pageSize) {
        Page<PostCardDTO> postCards = postService.getPostCardsWithPagination(offset, pageSize);
        return ResponseEntity.ok(postCards);
    }

    @GetMapping("/get/pagination/{offset}/{pageSize}")
    public ResponseEntity<Page<PostCardDTO>> getPostCardsWithFilters(
            @PathVariable int offset,
            @PathVariable int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String topicName,
            @RequestParam(required = false) String tagName) {

        Page<PostCardDTO> postCards = postService.getPostCardsWithFilters(
                username, topicName, tagName, offset, pageSize
        );
        return ResponseEntity.ok(postCards);
    }

    //gets post with post id
    @ResponseBody
    @GetMapping(value = "/get/{id}/**")
    public ResponseEntity<Post> getPost(@PathVariable("id") String id) {
        long new_id = Long.parseLong(id);
        return new ResponseEntity<>(postService.getPost(new_id), HttpStatus.OK);

    }

    @GetMapping("/get/{postId}/related")
    public ResponseEntity<List<PostDto>> getRelatedPosts(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "5") int limit) {
        List<PostDto> relatedPosts = postService.findRelatedPosts(postId, limit);
        return ResponseEntity.ok(relatedPosts);
    }


    //Creates post
    @PostMapping("/new_post")
    public ResponseEntity createPost(@RequestBody PostBodyRequest postBodyRequest) {
        Post post = postService.createPost(postBodyRequest);
        return new ResponseEntity(post, HttpStatus.OK);
    }

    // Updates a Post
    @ResponseBody
    @PutMapping(value="/update/{id}")
    public ResponseEntity updatePost(@PathVariable("id") String id, @RequestBody PostBodyRequest postBodyRequest) {
        long post_id = Long.valueOf(id);

        return new ResponseEntity(postService.updatePost(post_id, postBodyRequest),HttpStatus.OK);
    }

    // Delete post
    @ResponseBody
    @DeleteMapping(value =  "/delete/{id}")
    public ResponseEntity deletePost(@PathVariable("id") String id) {
        long post_id = Long.valueOf(id);
        return new ResponseEntity(postService.deletePost(post_id),HttpStatus.OK);
    }





}



