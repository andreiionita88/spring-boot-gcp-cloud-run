package com.example.demo.posts;

import java.time.Instant;

public class PostDTO {

    private String text;
    private Long createdBy;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
