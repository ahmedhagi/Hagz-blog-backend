package com.hagz.blog.controller;

import com.hagz.blog.model.Post;
import com.hagz.blog.payload.request.PostBodyRequest;
import com.hagz.blog.services.CommentService;
import com.hagz.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @ResponseBody
    @GetMapping( value = "/get/pagination/{offset}/{pageSize}/")
    public ResponseEntity<Page<Post>> getPostsWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Post> posts = postService.getPostWithPagination(offset,pageSize);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //Get All Posts with topic
   @ResponseBody
    @GetMapping(value = "/get/topic/{offset}/{pageSize}/{name}")
    public ResponseEntity<Page<Post>> getPostsByTopic(
            @PathVariable("name") String name,
            @PathVariable int offset,
            @PathVariable int pageSize) {

        return new ResponseEntity<>(postService.postsByTopic(name,offset,pageSize), HttpStatus.OK);

    }

    //Gets all Posts with username
    @ResponseBody
    @GetMapping(value = "/get/username/{offset}/{pageSize}/{username}")
    public ResponseEntity<Page<Post>> getPostsByUsername(
            @PathVariable("username") String username,
            @PathVariable int offset,
            @PathVariable int pageSize
            ) {

        return new ResponseEntity<>(postService.postByUsername(username,offset,pageSize), HttpStatus.OK);

    }

    //Gets all Posts with Tag id
    @ResponseBody
    @GetMapping(value = "/get/tag/{offset}/{pageSize}/{tag}")
    public ResponseEntity<Page<Post>>  getPostsByTag(
            @PathVariable("tag") String tagName,
            @PathVariable int offset,
            @PathVariable int pageSize
    ) {

        return new ResponseEntity<>(postService.postsByTag(tagName,offset,pageSize), HttpStatus.OK);

    }

    //gets post with post id
    @ResponseBody
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") String id) {
        long new_id = Long.valueOf(id);
        return new ResponseEntity<>(postService.getPost(new_id), HttpStatus.OK);

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



