package com.sharif.eshop.service.Image;

import com.sharif.eshop.dto.ImageDto;
import com.sharif.eshop.model.Image;
import com.sharif.eshop.model.Product;
import com.sharif.eshop.repository.ImageRepository;
import com.sharif.eshop.service.product.IProductService;
import com.sharif.eshop.service.product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;
    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image with id: " + imageId + " not found"));
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new EntityNotFoundException("Image with id: " + imageId + " not found");
                });
    }

    @Override
    public void updateImageById(MultipartFile file, Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image with id: " + imageId + " not found"));
        try {
            image.setFilename(file.getOriginalFilename());
            image.setFiletype(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating image with id: " + imageId+"\n" + e.getMessage());
        }

    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> saveImages =  new ArrayList<>() ;
        for ( MultipartFile file :files){
            try {
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/" ;
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFilename(savedImage.getFilename());
                imageDto.setDownloadUrl(downloadUrl);
                saveImages.add(imageDto);
            } catch (Exception e) {
                throw new RuntimeException("Error while saving image with product id: " + productId + "\n" + e.getMessage());
            }

        }
        return saveImages;
    }

    @Override
    public List<ImageDto> getImagesByProductId(Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new EntityNotFoundException("Product with id: " + productId + " not found");
        }
        List<Image> images = imageRepository.findByProductId(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (Image image : images) {
            ImageDto imageDto = new ImageDto();
            imageDto.setId(image.getId());
            imageDto.setFilename(image.getFilename());
            imageDto.setDownloadUrl(image.getDownloadUrl());
            imageDtos.add(imageDto);
        }
        return imageDtos;
    }
}
