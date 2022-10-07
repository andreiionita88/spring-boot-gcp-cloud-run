package com.example.demo.posts;

import java.io.Serial;
import java.io.Serializable;

public class PostDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7156526077883281623L;

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
