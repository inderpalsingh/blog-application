package com.blogapi.controllers;

import com.blogapi.dto.CategoryDto;
import com.blogapi.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    public CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto cat = categoryService.createCat(categoryDto);
        return new ResponseEntity<>(cat, HttpStatus.CREATED);
    }

    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer catId, @RequestBody CategoryDto categoryDto){
        CategoryDto updatedCat = categoryService.updateCat(catId, categoryDto);
        return new ResponseEntity<>(updatedCat, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        return new ResponseEntity<>(allCategory,HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable Integer catId){
        CategoryDto singleCat = categoryService.getSingleCat(catId);
        return new ResponseEntity<>(singleCat, HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer catId){
        categoryService.deleteCat(catId);
        return ResponseEntity.noContent().build();
    }
}
