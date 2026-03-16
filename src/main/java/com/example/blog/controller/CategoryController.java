package com.example.blog.controller;

import com.example.blog.DTO.CategoryDTO.CategoryPostDTO;
import com.example.blog.DTO.CategoryDTO.CategoryPutDTO;
import com.example.blog.DTO.CategoryDTO.CategoryResponseDTO;
import com.example.blog.DTO.PostDTO.PostResponseDTO;
import com.example.blog.entity.Category;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
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
    public CategoryResponseDTO createCategory(@RequestBody CategoryPostDTO dto) {
        Category category = new Category();
        category.setName(dto.name);
        categoryRepository.save(category);
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    @GetMapping(path = "/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) throw new RuntimeException("Category does not exist");
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(
                category -> {
                    return new CategoryResponseDTO(category.getId(), category.getName());
                }
        ).toList();
    }

    @GetMapping(path = "/{id}/posts")
    public List<PostResponseDTO> getPostsByCategoryId(@PathVariable Long id) {
        return postRepository.findByCategoryId(id).stream().map(post -> {
            return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getId(), post.getCreatedAt(), post.getCategory().getId());
        }).toList();
    }
    @DeleteMapping(path = "/{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        if (categoryRepository.existsById(id)) categoryRepository.deleteById(id);
    }
    @PutMapping(path = "/{id}")
    public CategoryResponseDTO updateCategoryById(@PathVariable Long id, @RequestBody CategoryPutDTO dto) {
        Category oldCategory = categoryRepository.findById(id).orElse(null);
        if (oldCategory == null) throw new RuntimeException("Category does not exist");
        oldCategory.setName(dto.name);
        return new CategoryResponseDTO(oldCategory.getId(), oldCategory.getName());
    }
}
