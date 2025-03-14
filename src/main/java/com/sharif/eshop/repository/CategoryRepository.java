package com.sharif.eshop.repository;

import com.sharif.eshop.model.CartItem;
import com.sharif.eshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name,'%'))")
    List<Category> findByName(String name);
    boolean existsByName(String name);


}
