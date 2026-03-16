package com.example.blog.controller;

import com.example.blog.DTO.PostDTO.PostResponseDTO;
import com.example.blog.DTO.UserDTO.UserPostDTO;
import com.example.blog.DTO.UserDTO.UserPutDTO;
import com.example.blog.DTO.UserDTO.UserResponseDTO;
import com.example.blog.entity.UserAccount;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserAccountRepository;
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
    public UserResponseDTO createUser(@RequestBody UserPostDTO dto) {
        UserAccount user = new UserAccount();
        user.setName(dto.name);
        user.setPassword(dto.password);
        user.setEmail(dto.email);
        userAccountRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        if (userAccountRepository.existsById(id)) userAccountRepository.deleteById(id);
        else throw new RuntimeException("user does not exist");
    }

    @GetMapping
    public List<UserResponseDTO> getAllUser() {
        return userAccountRepository.findAll().stream().map(user -> {
            return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
        }).toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        UserAccount user = userAccountRepository.findById(id).orElse(null);
        if (user == null) throw new RuntimeException("no user with that id");
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @GetMapping("/{id}/posts")
    public List<PostResponseDTO> getPostsByUserId(@PathVariable Long id) {
        return postRepository.findByAuthorId(id).stream().map(post -> {
            return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getId(), post.getCreatedAt(), post.getCategory().getId());
        }).toList();
    }


    @PutMapping("/{id}")
    public UserResponseDTO updateUserById(@PathVariable Long id, @RequestBody UserPutDTO newUser) {
        UserAccount oldUser = userAccountRepository.findById(id).orElse(null);
        if (oldUser == null) throw new RuntimeException("user does not exist");
        oldUser.setEmail(newUser.email);
        oldUser.setPassword(newUser.password);
        oldUser.setName(newUser.name);
        userAccountRepository.save(oldUser);
        return new UserResponseDTO(
                oldUser.getId(),
                oldUser.getName(),
                oldUser.getEmail()
        );
    }
}
