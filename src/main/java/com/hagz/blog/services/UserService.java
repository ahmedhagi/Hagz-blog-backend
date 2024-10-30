package com.hagz.blog.services;


import com.hagz.blog.model.Topic;
import com.hagz.blog.model.User;
import com.hagz.blog.payload.request.TopicRequest;
import com.hagz.blog.payload.request.UserRequest;
import com.hagz.blog.repository.UserRepository;
import com.hagz.blog.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

   @Transactional
    public User updateUser(UserRequest userRequest) {

        User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(
                () -> new RuntimeException("Error: User is not found."));

        userMapper.updateUserFromRequest(userRequest,user);

        userRepository.save(user);

        return user;

    }

    public User getUser(String username){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Error: User is not found."));
        return user;
    }




}