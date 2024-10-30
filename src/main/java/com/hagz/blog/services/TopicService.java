package com.hagz.blog.services;


import com.hagz.blog.model.Tag;
import com.hagz.blog.model.Topic;
import com.hagz.blog.payload.request.TagRequest;
import com.hagz.blog.payload.request.TopicRequest;
import com.hagz.blog.repository.TagRepository;
import com.hagz.blog.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public List<Topic> showAll() {

        List<Topic> topics = topicRepository.findAll();
        return topics.stream().collect(Collectors.toList());
    }

    @Transactional
    public List<Tag> showTags(String topicName) {

        List<Tag> topics = topicRepository.getTagsFromTopic(topicName);
        return topics.stream().collect(Collectors.toList());
    }

    public Topic topicRequestToTopic(TopicRequest topicRequest){
        Topic topic = new Topic();
        topic.setName(topicRequest.getName());
        return topic;

    }

    @Transactional
    public void createTopic(TopicRequest topicRequest){
        Topic topic = topicRequestToTopic(topicRequest);
        topicRepository.save(topic);
    }

    @Transactional
    public void addTags(String topicName, TagRequest tagRequest){

        Topic topic = topicRepository.findByName(topicName)
                .orElseThrow(() -> new RuntimeException("Error: topic is not found."));;

        tagRequest.getTags().forEach( tagName -> {

            Tag newTag =  tagRepository.findByName(tagName)
                    .orElseThrow(() -> new RuntimeException("Error: tag is not found."));

            topic.getTags().add(newTag);
        });

    }
}
