package com.sharif.eshop.service.product;

import com.sharif.eshop.dto.ProductDto;
import com.sharif.eshop.model.Product;
import com.sharif.eshop.request.AddProductRequest;
import com.sharif.eshop.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    Product getProductById(Long productId);
    void  deleteProductById(Long productId);


    List<Product> getAllProducts();
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByBrand(String brand);
//    List<Product> getProductsByPriceRange(BigDecimal min, BigDecimal max);
    List<Product> getProductByName(String name);


    List<ProductDto> getConvertedDto(List<Product> products);

    ProductDto convertToDto(Product product);
}
