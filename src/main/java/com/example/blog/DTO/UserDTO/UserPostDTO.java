package com.example.blog.DTO.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserPostDTO {
    @NotBlank
    @Length(max = 30)
    public String name;
    @Email
    @NotBlank
    public String email;
    @NotBlank
    public String password;
}
