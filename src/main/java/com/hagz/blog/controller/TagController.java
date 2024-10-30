package com.hagz.blog.controller;

import com.hagz.blog.model.Tag;
import com.hagz.blog.payload.request.TagRequest;
import com.hagz.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tags/")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity createTag(@RequestBody TagRequest tags) {
        tagService.createTags(tags);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tag>> showAllTags() {
        return new ResponseEntity<>(tagService.showAll(), HttpStatus.OK);
    }

}
