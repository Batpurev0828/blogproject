package com.example.blog.DTO.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserPutDTO {
    @NotBlank
    @Length(max = 50)
    public String name;
    @Email
    @NotBlank
    public String email;
    @NotBlank
    public String password;
    public UserPutDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
