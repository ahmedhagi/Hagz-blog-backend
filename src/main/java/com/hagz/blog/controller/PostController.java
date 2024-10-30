package com.hagz.blog.controller;

import com.hagz.blog.model.Post;
import com.hagz.blog.payload.request.CommentRequest;
import com.hagz.blog.payload.request.PostRequest;
import com.hagz.blog.services.CommentService;
import com.hagz.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //Creates post
    @PostMapping("/new_post")
    public ResponseEntity createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        return new ResponseEntity(post, HttpStatus.OK);
    }

    //shows all posts
    @GetMapping("/all")
    public ResponseEntity<List<Post>> showAllPosts() {
        return new ResponseEntity<>(postService.showAll(), HttpStatus.OK);
    }

    //Get All Posts with topic
   @ResponseBody
    @GetMapping(value = "/get/topic/{name}")
    public ResponseEntity<List<Post>> getPostsByTopic(@PathVariable("name") String name) {

        return new ResponseEntity<>(postService.postsByTopic(name), HttpStatus.OK);

    }

    //Gets all Posts with username
    @ResponseBody
    @GetMapping(value = "/get/username/{username}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable("username") String username) {

        return new ResponseEntity<>(postService.postByUsername(username), HttpStatus.OK);

    }

    //Gets all Posts with Tag id
    @ResponseBody
    @GetMapping(value = "/get/tag/{tag}")
    public ResponseEntity<List<Post>>  getPostsByTag(@PathVariable("tag") String tagName) {

        return new ResponseEntity<>(postService.postsByTag(tagName), HttpStatus.OK);

    }

    //gets post with post id
    @ResponseBody
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") String id) {
        long new_id = Long.valueOf(id);
        return new ResponseEntity<>(postService.getPost(new_id), HttpStatus.OK);

    }

    // Updates a Post
    @ResponseBody
    @PutMapping(value="/update/{id}")
    public ResponseEntity updatePost(@PathVariable("id") String id, @RequestBody PostRequest postrequest) {
        long post_id = Long.valueOf(id);

        return new ResponseEntity(postService.updatePost(post_id, postrequest),HttpStatus.OK);
    }

    // Delete post
    @ResponseBody
    @DeleteMapping(value =  "/delete/{id}")
    public ResponseEntity deletePost(@PathVariable("id") String id) {
        long post_id = Long.valueOf(id);
        return new ResponseEntity(postService.deletePost(post_id),HttpStatus.OK);
    }





}



