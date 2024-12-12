package com.hagz.blog.utils;

import com.hagz.blog.model.Post;
import com.hagz.blog.model.Tag;
import com.hagz.blog.model.Topic;
import com.hagz.blog.payload.request.PostBodyRequest;
import com.hagz.blog.payload.request.TagRequest;
import com.hagz.blog.repository.TagRepository;
import com.hagz.blog.repository.TopicRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostMapper {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TagRepository tagRepository;

    @Mappings({
            @Mapping(target = "topic", source = "topicName", qualifiedByName="convertTopic"),
            @Mapping(target = "tags", source = "tagSet", qualifiedByName="convertTags")
    }
    )
    public abstract void postUserFromRequest(PostBodyRequest postRequest, @MappingTarget Post post);


    @Named("convertTopic")
    public Topic convertTopic(String topicName){
        if(topicName != null)
        {

            Topic new_topic = topicRepository.findByNameIgnoreCase(topicName)
                    .orElseThrow(() -> new RuntimeException("Error: topic is not found."));
           return new_topic;
        }
        return null;
    }

    @Named("convertTags")
    public HashSet<Tag> convertTags(Set<String> tagSet ){
        if(tagSet != null)
        {
            TagRequest tagRequest = new TagRequest(tagSet.stream().toList());
            HashSet<Tag> tags = new HashSet<Tag>();
            tagRequest.getTags().forEach(new_tag -> {
                Tag tag = tagRepository.findByName(new_tag)
                        .orElseThrow(() -> new RuntimeException("Error: tag is not found."));
                tags.add(tag);
            });
            return tags;
        }
        return null;
    }



}
