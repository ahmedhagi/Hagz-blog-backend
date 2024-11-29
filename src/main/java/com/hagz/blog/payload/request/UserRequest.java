package com.hagz.blog.payload.request;

public class UserRequest {

    private String username;

    private String bio;

    private String imageUrl;


    public UserRequest(String username, String bio, String imageUrl) {
        this.username = username;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
