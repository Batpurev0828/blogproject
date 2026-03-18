package com.example.blog.controller;

import com.example.blog.DTO.PostDTO.PostResponseDTO;
import com.example.blog.DTO.UserDTO.UserPostDTO;
import com.example.blog.DTO.UserDTO.UserPutDTO;
import com.example.blog.DTO.UserDTO.UserResponseDTO;
import com.example.blog.api.ApiResponse;
import com.example.blog.entity.UserAccount;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {
    private final UserAccountRepository userAccountRepository;
    private final PostRepository postRepository;

    public UserAccountController(UserAccountRepository userAccountRepository, PostRepository postRepository) {
        this.userAccountRepository = userAccountRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody UserPostDTO dto) {
        if (userAccountRepository.existsByEmail(dto.email)) {
            ApiResponse<UserResponseDTO> response = new ApiResponse<>(409, "email already in use", null);
            return ResponseEntity.status(409).body(response);
        }
        UserAccount user = new UserAccount();
        user.setName(dto.name);
        user.setPassword(dto.password);
        user.setEmail(dto.email);
        userAccountRepository.save(user);
        UserResponseDTO data = new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
        ApiResponse<UserResponseDTO> response = new ApiResponse<UserResponseDTO>(200, "success", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable Long id) {
        if (!userAccountRepository.existsById(id)) {
            ApiResponse<Void> response = new ApiResponse<>(404, "user not found", null);
            return ResponseEntity.status(404).body(response);
        }
        userAccountRepository.deleteById(id);
        ApiResponse<Void> response = new ApiResponse<>(200, "success", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUser() {
        List<UserResponseDTO> users = userAccountRepository.findAll().stream().map(user -> {
            return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
        }).toList();
        ApiResponse<List<UserResponseDTO>> response = new ApiResponse<>(200, "success", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserAccount user = userAccountRepository.findById(id).orElse(null);
        if (user == null) {
            ApiResponse<UserResponseDTO> response = new ApiResponse<>(404, "could not find user", null);
            return ResponseEntity.status(404).body(response);
        }
        UserResponseDTO dto = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(200, "success", dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getPostsByUserId(@PathVariable Long id) {
        List<PostResponseDTO> posts = postRepository.findByAuthorId(id).stream().map(post -> {
            return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getId(), post.getCreatedAt(), post.getCategory().getId());
        }).toList();
        ApiResponse<List<PostResponseDTO>> response = new ApiResponse<>(200, "success", posts);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUserById(@PathVariable Long id, @RequestBody UserPutDTO newUser) {
        UserAccount oldUser = userAccountRepository.findById(id).orElse(null);
        if (oldUser == null) {
            ApiResponse<UserResponseDTO> response = new ApiResponse<>(404, "could not find user", null);
            return ResponseEntity.status(404).body(response);
        }
        if (userAccountRepository.existsByEmail(newUser.email)) {
            ApiResponse<UserResponseDTO> response = new ApiResponse<>(409, "email already in use", null);
            return ResponseEntity.status(404).body(response);
        }
        oldUser.setEmail(newUser.email);
        oldUser.setPassword(newUser.password);
        oldUser.setName(newUser.name);
        userAccountRepository.save(oldUser);
        UserResponseDTO dto = new UserResponseDTO(
                oldUser.getId(),
                oldUser.getName(),
                oldUser.getEmail()
        );
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(200, "success", dto);
        return ResponseEntity.ok(response);
    }
}
