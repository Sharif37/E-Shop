package com.sharif.eshop.repository;

import com.sharif.eshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
