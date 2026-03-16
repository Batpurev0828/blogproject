package com.example.blog.controller;

import com.example.blog.DTO.PostDTO.PostPostDTO;
import com.example.blog.DTO.PostDTO.PostPutDTO;
import com.example.blog.DTO.PostDTO.PostResponseDTO;
import com.example.blog.entity.Category;
import com.example.blog.entity.Post;
import com.example.blog.entity.UserAccount;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserAccountRepository;
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
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream().map(post -> {
            return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getId(), post.getCreatedAt(), post.getAuthor().getId());
        }).toList();
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPostById(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) throw new RuntimeException("post with the id does not exist");
        UserAccount author = post.getAuthor();
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), author.getId(), post.getCreatedAt(), post.getAuthor().getId());
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable Long id) {
        if (postRepository.existsById(id)) postRepository.deleteById(id);
    }

    @PostMapping
    public PostResponseDTO createPost(@RequestBody PostPostDTO dto) {
        UserAccount author = userAccountRepository.findById(dto.author_id).orElse(null);
        Category category = categoryRepository.findById(dto.category_id).orElse(null);
        if (author == null || category == null) throw new RuntimeException("category or author does not exist");
        Post post = new Post();
        post.setTitle(dto.title);
        post.setContent(dto.content);
        post.setAuthor(author);
        post.setCategory(category);
        postRepository.save(post);
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), author.getId(), post.getCreatedAt(), category.getId());
    }

    @PutMapping("/{id}")
    public PostResponseDTO updatePostById(@PathVariable Long id, @RequestBody PostPutDTO newPost) {
        Post oldPost = postRepository.findById(id).orElse(null);
        Category category = categoryRepository.findById(newPost.category_id).orElse(null);
        if (oldPost == null) throw new RuntimeException("Post does not exist");
        if (category == null) throw new RuntimeException("Category does not exist");
        oldPost.setContent(newPost.content);
        oldPost.setTitle(newPost.title);
        oldPost.setCategory(category);
        postRepository.save(oldPost);
        UserAccount author = oldPost.getAuthor();
        return new PostResponseDTO(oldPost.getId(), oldPost.getTitle(), oldPost.getContent(), author.getId(), oldPost.getCreatedAt(), category.getId());
    }
}
