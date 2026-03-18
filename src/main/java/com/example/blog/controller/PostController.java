package com.example.blog.controller;

import com.example.blog.DTO.PostDTO.PostPostDTO;
import com.example.blog.DTO.PostDTO.PostPutDTO;
import com.example.blog.DTO.PostDTO.PostResponseDTO;
import com.example.blog.api.ApiResponse;
import com.example.blog.entity.Category;
import com.example.blog.entity.Post;
import com.example.blog.entity.UserAccount;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserAccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserAccountRepository userAccountRepository;
    private final CategoryRepository categoryRepository;

    public PostController(PostRepository postRepository, UserAccountRepository userAccountRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userAccountRepository = userAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getAllPosts() {
        List<PostResponseDTO> posts = postRepository.findAll().stream().map(post -> {
            return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getId(), post.getCreatedAt(), post.getAuthor().getId());
        }).toList();
        ApiResponse<List<PostResponseDTO>> response = new ApiResponse<>(200, "success", posts);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            ApiResponse<PostResponseDTO> response = new ApiResponse<>(404, "could not find post", null);
        }
        UserAccount author = post.getAuthor();
        PostResponseDTO dto = new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), author.getId(), post.getCreatedAt(), post.getAuthor().getId());
        ApiResponse<PostResponseDTO> response = new ApiResponse<>(200, "success", dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePostById(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            ApiResponse<Void> response = new ApiResponse<>(404, "could not find post", null);
            return ResponseEntity.status(404).body(response);
        }
        postRepository.deleteById(id);
        ApiResponse<Void> dto = new ApiResponse<>(200, "success", null);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(@Valid @RequestBody PostPostDTO dto) {
        UserAccount author = userAccountRepository.findById(dto.author_id).orElse(null);
        Category category = categoryRepository.findById(dto.category_id).orElse(null);
        if (author == null) {
            ApiResponse<PostResponseDTO> response = new ApiResponse<>(404, "the author does not exist", null);
            return ResponseEntity.status(404).body(response);
        }
        if (category == null) {
            ApiResponse<PostResponseDTO> response = new ApiResponse<>(404, "the category does not exist", null);
            return ResponseEntity.status(404).body(response);
        }
        Post post = new Post();
        post.setTitle(dto.title);
        post.setContent(dto.content);
        post.setAuthor(author);
        post.setCategory(category);
        postRepository.save(post);
        PostResponseDTO createdPost = new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), author.getId(), post.getCreatedAt(), category.getId());
        ApiResponse<PostResponseDTO> response = new ApiResponse<>(200, "success", createdPost);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePostById(@PathVariable Long id,@Valid @RequestBody PostPutDTO newPost) {
        Post oldPost = postRepository.findById(id).orElse(null);
        Category category = categoryRepository.findById(newPost.category_id).orElse(null);
        if (oldPost == null) {
            ApiResponse<PostResponseDTO> response = new ApiResponse<>(404, "post does not exist", null);
            return ResponseEntity.status(404).body(response);
        }
        if (category == null) {
            ApiResponse<PostResponseDTO> response = new ApiResponse<>(404, "category does not exist", null);
            return ResponseEntity.status(404).body(response);
        }
        oldPost.setContent(newPost.content);
        oldPost.setTitle(newPost.title);
        oldPost.setCategory(category);
        postRepository.save(oldPost);
        UserAccount author = oldPost.getAuthor();

        PostResponseDTO dto = new PostResponseDTO(oldPost.getId(), oldPost.getTitle(), oldPost.getContent(), author.getId(), oldPost.getCreatedAt(), category.getId());
        ApiResponse<PostResponseDTO> response = new ApiResponse<>(200, "success", dto);
        return ResponseEntity.ok(response);
    }
}
