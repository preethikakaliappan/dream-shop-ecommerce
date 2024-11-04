package com.dailycode.dream_shops.service.image;

import com.dailycode.dream_shops.Exception.ResourceNotFoundException;
import com.dailycode.dream_shops.dto.ImageDto;
import com.dailycode.dream_shops.model.Image;
import com.dailycode.dream_shops.model.ImageRepository;
import com.dailycode.dream_shops.model.Product;
import com.dailycode.dream_shops.service.product.ProductService;
import com.dailycode.dream_shops.service.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductService productService;



    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No image found with id:" +id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository :: delete, ()-> {
            throw new ResourceNotFoundException("NO image found with id:"+id);
        });

    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile > files, long productId) throws RuntimeException {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for(MultipartFile file: files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl ="/api/v1/images/image/download";
                String downloadUrl =buildDownloadUrl +image.getId();
                image.setDownloadUrl(downloadUrl);
               Image savedImage = imageRepository.save(image);

               savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
               imageRepository.save(savedImage);

               ImageDto imageDto = new ImageDto();
               imageDto.setImageId(savedImage.getId());
               imageDto.setImageName(savedImage.getFileName());
               imageDto.setDownLoadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());

            }



        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
