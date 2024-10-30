package com.hagz.blog.services;

import com.hagz.blog.model.Post;
import com.hagz.blog.model.Tag;
import com.hagz.blog.payload.request.TagRequest;
import com.hagz.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public List<Tag> showAll() {

        List<Tag> tags = tagRepository.findAll();
        return tags.stream().collect(Collectors.toList());
    }

    @Transactional
    public void createTags(TagRequest tagRequest){

        List<Tag> tags = tagRequestToTags(tagRequest);

        tags.forEach( tag -> {
            tagRepository.save(tag);
        });

    }

    public List<Tag> tagRequestToTags(TagRequest tagRequest){

        ArrayList<Tag> tags = new ArrayList<Tag>();

        tagRequest.getTags().forEach( new_tag -> {
            Tag tag = new Tag();
            tag.setName(new_tag);
            tags.add(tag);
        });


        return tags;

    }
}
