package com.sharif.eshop.service.category;

import com.sharif.eshop.model.Category;
import com.sharif.eshop.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {

        return  Optional.of(category)
                        .filter(newCategory ->
                         !categoryRepository.existsByName(newCategory.getName()))
                        .map(categoryRepository::save)
                        .orElseThrow(() -> new EntityNotFoundException("Category Name :  " + category.getName() + " Already Exists! "));
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(findCategoryById(categoryId)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new EntityNotFoundException("Category with category id " + categoryId + " not found!. "));

    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new EntityNotFoundException("Category With Category Id :" + categoryId + " Not Found  ");

        });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category with Id: " + categoryId + " Not Found!"));
    }
}
