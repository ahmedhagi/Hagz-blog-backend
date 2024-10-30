package com.hagz.blog.utils;

import com.hagz.blog.model.Post;
import com.hagz.blog.payload.request.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    void postUserFromRequest(PostRequest postRequest, @MappingTarget Post post);
}
