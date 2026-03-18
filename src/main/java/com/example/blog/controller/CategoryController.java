package com.example.blog.controller;

import com.example.blog.DTO.CategoryDTO.CategoryPostDTO;
import com.example.blog.DTO.CategoryDTO.CategoryPutDTO;
import com.example.blog.DTO.CategoryDTO.CategoryResponseDTO;
import com.example.blog.DTO.PostDTO.PostResponseDTO;
import com.example.blog.api.ApiResponse;
import com.example.blog.entity.Category;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public CategoryController(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryPostDTO dto) {

        if (categoryRepository.existsByName(dto.name)) {
            ApiResponse<CategoryResponseDTO> response = new ApiResponse<>(409, "name is already used", null);
            return ResponseEntity.status(409).body(response);
        }

        Category category = new Category();
        category.setName(dto.name);
        categoryRepository.save(category);

        CategoryResponseDTO created = new CategoryResponseDTO(category.getId(), category.getName());

        ApiResponse<CategoryResponseDTO> response = new ApiResponse<>(201, "category created", created);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable Long id) {

        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            ApiResponse<CategoryResponseDTO> response = new ApiResponse<>(404, "category not found", null);
            return ResponseEntity.status(404).body(response);
        }

        CategoryResponseDTO data = new CategoryResponseDTO(category.getId(), category.getName());

        ApiResponse<CategoryResponseDTO> response = new ApiResponse<>(200, "success", data);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories() {

        List<CategoryResponseDTO> categories = categoryRepository.findAll().stream().map(c -> new CategoryResponseDTO(c.getId(), c.getName())).toList();

        ApiResponse<List<CategoryResponseDTO>> response = new ApiResponse<>(200, "success", categories);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getPostsByCategoryId(@PathVariable Long id) {

        if (!categoryRepository.existsById(id)) {
            ApiResponse<List<PostResponseDTO>> response = new ApiResponse<>(404, "category not found", null);
            return ResponseEntity.status(404).body(response);
        }

        List<PostResponseDTO> posts = postRepository.findByCategoryId(id).stream().map(post -> new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getId(), post.getCreatedAt(), post.getCategory().getId())).toList();

        ApiResponse<List<PostResponseDTO>> response = new ApiResponse<>(200, "success", posts);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoryById(@PathVariable Long id) {

        if (!categoryRepository.existsById(id)) {
            ApiResponse<Void> response = new ApiResponse<>(404, "category not found", null);
            return ResponseEntity.status(404).body(response);
        }

        categoryRepository.deleteById(id);

        ApiResponse<Void> response = new ApiResponse<>(200, "category deleted", null);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryPutDTO dto) {

        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            ApiResponse<CategoryResponseDTO> response = new ApiResponse<>(404, "category not found", null);
            return ResponseEntity.status(404).body(response);
        }

        category.setName(dto.name);
        categoryRepository.save(category);

        CategoryResponseDTO data = new CategoryResponseDTO(category.getId(), category.getName());

        ApiResponse<CategoryResponseDTO> response = new ApiResponse<>(200, "category updated", data);

        return ResponseEntity.ok(response);
    }
}