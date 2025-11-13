package com.blogapi.services.impl;

import com.blogapi.dto.CategoryDto;
import com.blogapi.entities.Category;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.CategoryRepository;
import com.blogapi.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCat(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category saved = categoryRepository.save(category);
        return modelMapper.map(saved, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCat(Integer catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id ", catId));
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        Category saved = categoryRepository.save(category);
        return modelMapper.map(saved, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(category -> modelMapper.map(category,CategoryDto.class)).toList();
    }

    @Override
    public CategoryDto getSingleCat(Integer catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id ", catId));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public void deleteCat(Integer catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category id ", catId));
        categoryRepository.delete(category);
    }
}
