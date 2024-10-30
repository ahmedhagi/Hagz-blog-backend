package com.hagz.blog.controller;

import com.hagz.blog.model.Tag;
import com.hagz.blog.model.Topic;
import com.hagz.blog.payload.request.TagRequest;
import com.hagz.blog.payload.request.TopicRequest;
import com.hagz.blog.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topics/")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping("/create")
    public ResponseEntity createTopic(@RequestBody TopicRequest topic) {
        topicService.createTopic(topic);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Topic>> showAllTopics() {
        return new ResponseEntity<>(topicService.showAll(), HttpStatus.OK);
    }

    @PostMapping("/add/{topic}")
    ResponseEntity addTag(@RequestBody TagRequest tagRequest, @PathVariable("topic") String topicName){

        topicService.addTags(topicName, tagRequest);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/all/tags/{topicName}")
    public ResponseEntity<List<Tag>> showTopicTags(@PathVariable("topicName") String topicName){

        return new ResponseEntity<>(topicService.showTags(topicName), HttpStatus.OK);
    }



}
