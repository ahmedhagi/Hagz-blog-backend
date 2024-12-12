package com.hagz.blog.payload.request;

import java.util.List;

public class TagRequest {
    public TagRequest(List<String> tags) {
        this.tags = tags;
    }

    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
