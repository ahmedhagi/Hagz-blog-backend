package com.hagz.blog.payload.response;

public class PostCardDTO {
    private Long id;
    private String slug;
    private String imageUrl;
    private String topicName;
    private String shortDesc;
    private String username;
    private Integer commentsCount;

    // Constructor for JPQL projection
    public PostCardDTO(Long id, String slug, String imageUrl,
                       String topicName, String shortDesc,
                       String username, Long commentsCount) {
        this.id = id;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.topicName = topicName;
        this.shortDesc = shortDesc;
        this.username = username;
        this.commentsCount = commentsCount != null ? commentsCount.intValue() : 0;
    }

    // Getters and setters

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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}