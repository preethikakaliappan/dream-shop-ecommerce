package com.dailycode.dream_shops.service.image;

import com.dailycode.dream_shops.dto.ImageDto;
import com.dailycode.dream_shops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, long productId);
    void updateImage(MultipartFile file, Long imageId);

}
