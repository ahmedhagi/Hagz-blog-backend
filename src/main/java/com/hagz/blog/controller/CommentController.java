package com.hagz.blog.controller;

import com.hagz.blog.model.Comment;
import com.hagz.blog.model.Post;
import com.hagz.blog.payload.request.CommentRequest;
import com.hagz.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment/")
public class CommentController {

    @Autowired
    private CommentService commentService;



    // Post new Comment
    @PostMapping(value =  "{id}/comment/")
    public ResponseEntity createComment(@RequestBody CommentRequest commentRequest, @PathVariable("id") String id) {
        long post_id = Long.valueOf(id);
        return new ResponseEntity(commentService.createComment(post_id, commentRequest),HttpStatus.OK);
    }

    // Get all Comments given post id
    @GetMapping(value =  "get/{id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("id") String id) {
        long post_id = Long.valueOf(id);

        return new ResponseEntity(commentService.showComments(post_id),HttpStatus.OK);
    }

    // Update a Comment
    @PutMapping(value =  "{id}/update")
    public ResponseEntity updateComment(@RequestBody CommentRequest commentRequest, @PathVariable("id") String id) {
        long comment_id = Long.valueOf(id);
        return new ResponseEntity(commentService.updateComment(comment_id, commentRequest.getContent()),HttpStatus.OK);
    }

    // Delete a Comment
    @DeleteMapping(value =  "{id}/delete")
    public ResponseEntity deleteComment(@PathVariable("id") String id) {
        long comment_id = Long.valueOf(id);

        return new ResponseEntity(commentService.deleteComment(comment_id),HttpStatus.OK);
    }


}


