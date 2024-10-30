package com.hagz.blog.controller;


import com.hagz.blog.model.User;
import com.hagz.blog.payload.request.UserRequest;
import com.hagz.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping(path="/get/{username}")
    public ResponseEntity getUser(@PathVariable("username") String username){
        User user = userService.getUser(username);

        return new ResponseEntity(user,HttpStatus.OK);
    }


    @PutMapping(path = "/update")
    public ResponseEntity updateUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity( userService.updateUser(userRequest),HttpStatus.OK);
    }




}
