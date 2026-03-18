package com.example.blog.DTO.CategoryDTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CategoryPostDTO {
    @NotBlank
    @Length(max = 30)
    public String name;
}
