package com.hagz.blog.utils;

import com.hagz.blog.model.User;
import com.hagz.blog.payload.request.UserRequest;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}
