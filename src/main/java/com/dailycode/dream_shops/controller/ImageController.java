package com.dailycode.dream_shops.controller;


import com.dailycode.dream_shops.Exception.ResourceNotFoundException;
import com.dailycode.dream_shops.dto.ImageDto;
import com.dailycode.dream_shops.model.Image;
import com.dailycode.dream_shops.model.Product;
import com.dailycode.dream_shops.response.ApiResponse;
import com.dailycode.dream_shops.service.image.ImageService;
import com.dailycode.dream_shops.service.image.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId){
        try {
            List<ImageDto> imageDtos= imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("Upload Success",imageDtos));
        } catch (RuntimeException e) {
             return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("upload Failed!",e.getMessage()));
        }
    }

   public ResponseEntity<Resource> downloadsImage(@PathVariable Long imageId) throws SQLException {
       Image image = imageService.getImageById(imageId);
       ByteArrayResource resource=new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
       return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+image.getFileName()+"\"")
               .body(resource);
   }
    @PutMapping("/image/{imageId}/update")
   public ResponseEntity<ApiResponse> UpdateImage(@PathVariable Long imageId, @RequestBody MultipartFile file){
       try {
           Image image = imageService.getImageById(imageId);
           if(image != null){
               imageService.updateImage(file, imageId);
               return ResponseEntity.ok(new ApiResponse("Update success", null));

           }
       } catch (ResourceNotFoundException e) {
           return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));

       }
       return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
   }

    @DeleteMapping ("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success", null));

            }
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));

        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }



}
