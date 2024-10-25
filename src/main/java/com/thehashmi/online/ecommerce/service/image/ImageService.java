package com.thehashmi.online.ecommerce.service.image;

import com.thehashmi.online.ecommerce.dto.ImageDto;
import com.thehashmi.online.ecommerce.model.Images;
import com.thehashmi.online.ecommerce.model.Product;
import com.thehashmi.online.ecommerce.repository.ImageRepository;
import com.thehashmi.online.ecommerce.service.product.IProductService;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService iProductService;
    @Override
    public Images getImageById(Long id) {
        Images image = imageRepository.findById(id).orElse(null);
        if(image == null){
            throw new NotFoundException("Image not found!");
        }
        return image;
    }

    @Override
    public void deleteImageById(Long id) {
        Images image = imageRepository.findById(id).orElse(null);
        if(image == null){
            throw new NotFoundException("Image not found!");
        }
        imageRepository.delete(image);
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files,Long productId) {
        Product product = iProductService.getProductById(productId);
        List<ImageDto> savedImagesDto = new ArrayList<ImageDto>();
        for (MultipartFile file : files){
            try{
                Images image = new Images();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                Images savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImagesDto.add(imageDto);

            }   catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Images image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
