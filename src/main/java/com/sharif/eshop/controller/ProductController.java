package com.sharif.eshop.controller;

import com.sharif.eshop.dto.ProductDto;
import com.sharif.eshop.model.Product;
import com.sharif.eshop.request.AddProductRequest;
import com.sharif.eshop.request.ProductUpdateRequest;
import com.sharif.eshop.response.ApiResponse;
import com.sharif.eshop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest newProduct) {
        try {
            Product product = productService.addProduct(newProduct);
            ProductDto convertedProduct = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Add product Successfully! ", convertedProduct ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error while adding product", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductDto> convertedProducts = productService.getConvertedDto(products);
            return ResponseEntity.ok(new ApiResponse("Fetch All product Successfully! ", convertedProducts ));
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(new ApiResponse("Error while fetching all products", e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto convertedProduct = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Fetch product Successfully! ", convertedProduct ));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error while fetching product", e.getMessage()));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product Successfully! ", null ));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error while deleting product", e.getMessage()));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProductById(@PathVariable Long productId, @RequestBody  ProductUpdateRequest newProduct) {
        try {
            Product product = productService.updateProduct(newProduct, productId);
            ProductDto convertedProduct = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Update product Successfully! ", convertedProduct ));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error while updating product", e.getMessage()));
        }
    }


}
