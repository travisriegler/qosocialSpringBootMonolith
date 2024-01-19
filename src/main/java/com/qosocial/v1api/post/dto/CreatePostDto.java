package com.qosocial.v1api.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePostDto {
    @NotBlank(message = "Text is required")
    @Size(min = 1, max = 200, message = "Text must be between 1 and 200 characters")
    private String textContent;

    public CreatePostDto() {
    }

    public CreatePostDto(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
