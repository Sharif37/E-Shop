package com.sharif.eshop.repository;

import com.sharif.eshop.dto.ProductDto;
import com.sharif.eshop.model.Category;
import com.sharif.eshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.plaf.basic.BasicIconFactory;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByCategoryName(String category);

    List<Product> findByBrandAndName(String brand, String name);

    List<Product> findByBrand(String brand);

//    List<Product> findByPriceRange(BigDecimal min, BigDecimal max);

    List<Product> findByName(String name);

    boolean existsByNameAndBrand(String name, String brand);


}
