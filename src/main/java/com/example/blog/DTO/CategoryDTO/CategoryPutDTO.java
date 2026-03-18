package com.example.blog.DTO.CategoryDTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CategoryPutDTO {
    @NotBlank
    @Length(max = 30)
    public String name;
    public CategoryPutDTO(String name) {
        this.name = name;
    }
}
