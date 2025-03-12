package com.sharif.eshop.controller;


import com.sharif.eshop.dto.ImageDto;
import com.sharif.eshop.model.Image;
import com.sharif.eshop.response.ApiResponse;
import com.sharif.eshop.service.Image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImages(@RequestParam("files") List<MultipartFile> files, @RequestParam("productId") Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Images Uploaded Successfully..", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: Image uploading fails! ", e.getMessage()));

        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFiletype())).
                header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFilename() + "\"").body(resource);
    }

    @DeleteMapping("/image/{imageId}/delete")
   public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Image Deleted Successfully..",null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: Image deleting fails! ", e.getMessage()));
        }
   }


   @PutMapping("/image/{imageId}/update")
   public ResponseEntity<ApiResponse> updateImage(@RequestBody MultipartFile file, @PathVariable("imageId") Long imageId) {
        try {
            imageService.updateImageById(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Image Updated Successfully..",null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: Image updating fails! ", e.getMessage()));
        }
   }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getImagesByProductId(@PathVariable Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.getImagesByProductId(productId);
            return ResponseEntity.ok(new ApiResponse("Images Retrieved Successfully..", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: Image retrieving fails! ", e.getMessage()));
        }
    }



}
