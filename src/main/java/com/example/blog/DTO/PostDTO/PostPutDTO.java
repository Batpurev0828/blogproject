package com.example.blog.DTO.PostDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public class PostPutDTO {
    @NotBlank
    @Length(max = 50)
    public String title;
    @NotBlank
    public String content;
    @NotNull
    @PositiveOrZero
    public Long category_id;
}
