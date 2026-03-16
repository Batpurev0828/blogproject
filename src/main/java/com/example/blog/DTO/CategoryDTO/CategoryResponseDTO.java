package com.example.blog.DTO.CategoryDTO;

public class CategoryResponseDTO {
    public Long id;
    public String name;
    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
