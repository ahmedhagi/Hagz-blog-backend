package com.hagz.blog.payload.request;

import com.hagz.blog.model.Tag;
import com.hagz.blog.model.Topic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostBodyRequest {
    private String title;

    private String content;

    private String topicName;

    private String shortDesc;

    private String imageUrl;

    private Set<String> tagSet;

    public PostBodyRequest(String title, String content, List<String> tagSet, String topicName, String shortDesc, String imageUrl) {
        this.title = title;
        this.content = content;
        this.topicName = topicName;
        this.tagSet = new HashSet<String>(tagSet);
        this.shortDesc = shortDesc;
        this.imageUrl = imageUrl;

    }

    public PostBodyRequest() {

    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<String> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<String> tagSet) {
        this.tagSet = tagSet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
