package com.hagz.blog.payload.request;

import java.util.List;

public class TagRequest {

    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
