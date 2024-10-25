package com.thehashmi.online.ecommerce.service.image;

import com.thehashmi.online.ecommerce.dto.ImageDto;
import com.thehashmi.online.ecommerce.model.Images;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IImageService {
    Images getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages( List<MultipartFile> files,Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
