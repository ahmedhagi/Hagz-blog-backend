package com.hagz.blog.utils;

import com.hagz.blog.payload.response.PostDto;
import com.hagz.blog.payload.response.TagDto;
import com.hagz.blog.model.Post;
import com.hagz.blog.model.Tag;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PostResponseMapper {

    /**
     * Convert Post entity to PostDto for API responses
     * Maps only the fields needed for post previews (related posts, lists, etc.)
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "shortDesc", source = "shortDesc"),
            @Mapping(target = "slug", source = "slug"),
            @Mapping(target = "imageUrl", source = "imageUrl"),
            @Mapping(target = "createdOn", source = "createdOn"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "topicName", expression = "java(post.getTopic() != null ? post.getTopic().getName() : null)"),
            @Mapping(target = "commentCount", expression = "java(post.getComments() != null ? post.getComments().size() : 0)"),
            @Mapping(target = "tags", source = "tags", qualifiedByName = "convertTagsToDto")
    })
    PostDto postToPostDto(Post post);

    /**
     * Convert list of Post entities to list of PostDto
     */
    List<PostDto> postsToPostDtos(List<Post> posts);

    /**
     * Convert Tag entity to simple TagDto
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    TagDto tagToTagDto(Tag tag);

    /**
     * Convert Set of Tags to Set of TagDtos
     */
    @Named("convertTagsToDto")
    default Set<TagDto> convertTagsToDto(Set<Tag> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(this::tagToTagDto)
                .collect(Collectors.toSet());
    }
}