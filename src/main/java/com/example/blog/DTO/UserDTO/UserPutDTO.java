package com.example.blog.DTO.UserDTO;

public class UserPutDTO {
    public Long id;
    public String name;
    public String email;
    public String password;
    public UserPutDTO(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
