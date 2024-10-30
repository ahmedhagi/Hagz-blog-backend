package com.hagz.blog.payload.request;

public class UserRequest {

    private String username;

    private String bio;

    private String imageURL;


    public UserRequest(String username, String bio, String imageURL) {
        this.username = username;
        this.bio = bio;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
