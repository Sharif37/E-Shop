package com.sharif.eshop.service.Image;

import com.sharif.eshop.dto.ImageDto;
import com.sharif.eshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    void  updateImageById(MultipartFile file , Long imageId);
    List<ImageDto> saveImages(List<MultipartFile> files , Long productId);
    List<ImageDto> getImagesByProductId(Long productId);



}
