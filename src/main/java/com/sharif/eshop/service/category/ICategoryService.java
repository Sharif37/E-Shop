package com.sharif.eshop.service.category;

import com.sharif.eshop.model.Category;

import java.util.List;

public interface ICategoryService {
        Category addCategory(Category category) ;
        Category updateCategory(Category category, Long categoryId) ;
        void deleteCategory(Long categoryId);
        List<Category> getAllCategories();
        List<Category> findCategoryByName(String name);
        Category findCategoryById(Long categoryId);

}
