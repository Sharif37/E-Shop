package com.sharif.eshop.service.product;

import com.sharif.eshop.dto.ImageDto;
import com.sharif.eshop.dto.ProductDto;
import com.sharif.eshop.model.*;
import com.sharif.eshop.repository.*;
import com.sharif.eshop.request.AddProductRequest;
import com.sharif.eshop.request.ProductUpdateRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductItemRepository orderItemRepository;

    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        if (existsByNameAndBrand(request.getName(), request.getBrand())) {
            throw new EntityExistsException("Product with name: " + request.getName() + " and brand: " + request.getBrand() + " already exists");
        }
        Category category = categoryRepository.findByName(request.getCategory().getName())
                .stream()
                .findFirst()  // Ensure we get only one category
                .orElseGet(() -> categoryRepository.save(new Category(request.getCategory().getName())));
        return productRepository.save(createProduct(request, category));

    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(request.getName(), request.getBrand(), request.getPrice(), request.getInventory(), request.getDescription(), category);

    }

    private boolean existsByNameAndBrand(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }




    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productId) {
       return productRepository.findById(productId)
               .map(existingProduct -> updateExitsingProduct(existingProduct, product))
               .map(productRepository::save)
               .orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " not found"));
    }

    private Product updateExitsingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        var category = categoryRepository.existsByName(request.getCategory().getName());
        if(category)
            existingProduct.setCategory(request.getCategory());
        else {
            Category newCategory = new Category(request.getCategory().getName());
            categoryRepository.save(newCategory);
            existingProduct.setCategory(newCategory);
        }
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(
               product -> {
                   List<CartItem> cartItems =cartItemRepository.findByProductId(productId);
                   cartItems.forEach(cartItem ->{
                           Cart cart = cartItem.getCart();
                           cart.removeItem(cartItem);
                           cartItemRepository.delete(cartItem);

                   });
                   List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
                     orderItems.forEach(orderItem -> {
                         orderItem.setProduct(null);
                         orderItemRepository.save(orderItem);
                     });
                     Optional.ofNullable(product.getCategory()).ifPresent(
                             category -> {
                                    category.getProducts().remove(product);
                             }
                     );
                     product.setCategory(null);

                     productRepository.deleteById(productId);


               },
                () -> {
                    throw new EntityNotFoundException("Product with id: " + productId + " not found");
                }
        );

    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " not found"));
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

//    public List<Product> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
//        return productRepository.findByPriceRange(min, max);
//    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<ProductDto> getConvertedDto(List<Product> products){
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
