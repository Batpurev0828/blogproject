package com.example.blog.DTO.UserDTO;

public class UserResponseDTO {
    public Long id;
    public String name;
    public String email;
    public UserResponseDTO(Long id, String name, String email) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
    public UserResponseDTO() {}
}
