package com.hagz.blog.services;

import com.hagz.blog.model.Comment;
import com.hagz.blog.model.Post;
import com.hagz.blog.payload.request.CommentRequest;
import com.hagz.blog.repository.CommentRepository;
import com.hagz.blog.repository.PostRepository;
import com.hagz.blog.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    //gets a post given a post id
    public Post getPost(long ID) {
        Post post = postRepository.findById(ID)
                .orElseThrow(() -> new RuntimeException("Error: post is not found."));
        return post;
    }

    //shows all comments on a post
    @Transactional
    public List<Comment> showComments(long PostId) {
        Post post = getPost(PostId);
        return post.getComments().stream().collect(Collectors.toList());
    }

    @Transactional
    public Comment createComment( long postId, CommentRequest commentRequest) {
        Comment comment = commentRequestToComment(postId, commentRequest);
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public Comment deleteComment( long commentID){
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new RuntimeException("Error: comment is not found."));
        commentRepository.delete(comment);
        return comment;
    }

    @Transactional
    public Comment updateComment(long commentID, String content){
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new RuntimeException("Error: post is not found."));
        comment.setContent(content);
        comment.setUpdatedOn(Instant.now());
        return comment;
    }


    public Comment commentRequestToComment(long postId, CommentRequest commentRequest){
        Comment comment = new Comment();

        comment.setContent(commentRequest.getContent());
        comment.setCreatedOn(Instant.now());
        comment.setUpdatedOn(Instant.now());

        //Gets the username
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal();
            comment.setUsername(userDetails.getUsername());
        }
        catch(Exception e){
            throw new IllegalArgumentException("User not login");
        }

        //adds comments to post
        Post currentPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Error: post is not found."));


        currentPost.getComments().add(comment);



        return comment;

    }

}


