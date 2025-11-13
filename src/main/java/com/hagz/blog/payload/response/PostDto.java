package com.hagz.blog.payload.response;

import java.time.Instant;
import java.util.Set;

public class PostDto {
    private Long id;
    private String title;
    private String shortDesc;
    private String slug;
    private String imageUrl;
    private Instant createdOn;
    private String username;
    private Integer commentCount;
    private String topicName;
    private Set<TagDto> tags;

    // Constructors
    public PostDto() {
    }

    public PostDto(Long id, String title, String shortDesc, String slug,
                   String imageUrl, Instant createdOn, String username) {
        this.id = id;
        this.title = title;
        this.shortDesc = shortDesc;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.createdOn = createdOn;
        this.username = username;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}