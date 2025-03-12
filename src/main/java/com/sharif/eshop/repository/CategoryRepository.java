package com.sharif.eshop.repository;

import com.sharif.eshop.model.CartItem;
import com.sharif.eshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    boolean existsByName(String name);


}
