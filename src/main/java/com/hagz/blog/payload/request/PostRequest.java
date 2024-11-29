package com.hagz.blog.payload.request;





import com.hagz.blog.model.Tag;
import com.hagz.blog.model.Topic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostRequest {

    private String title;
    private String content;
    
    private Topic topic;

    private String shortDesc;

    private String imageUrl;

    private Set<Tag> tags;

    public PostRequest(String title, String content, List<Tag> tags, Topic topic, String shortDesc, String imageUrl) {
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.tags = new HashSet<Tag>(tags);
        this.shortDesc = shortDesc;
        this.imageUrl = imageUrl;

    }

    public PostRequest() {

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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


    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
