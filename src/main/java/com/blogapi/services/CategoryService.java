package com.blogapi.services;

import com.blogapi.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCat(CategoryDto categoryDto);

    CategoryDto updateCat(Integer catId, CategoryDto categoryDto);

    List<CategoryDto> getAllCategory();

    CategoryDto getSingleCat(Integer catId);

    void deleteCat(Integer catId);
}
