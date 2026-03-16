package com.example.blog.DTO.PostDTO;

import java.time.LocalDateTime;

public class PostResponseDTO {
    public Long id;
    public String title;
    public String content;
    public Long author_id;
    public Long category_id;
    public LocalDateTime createdAt;
    public PostResponseDTO(Long id, String title, String content, Long author_id, LocalDateTime createdAt, Long category_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author_id = author_id;
        this.createdAt = createdAt;
        this.category_id = category_id;
    }
    public PostResponseDTO() {}
}
