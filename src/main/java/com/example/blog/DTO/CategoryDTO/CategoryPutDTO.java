package com.example.blog.DTO.CategoryDTO;

public class CategoryPutDTO {
    public String name;
    public CategoryPutDTO(Long id, String name) {
        this.name = name;
    }
}
