package com.dailycode.dream_shops.service.product;

import com.dailycode.dream_shops.model.Product;
import com.dailycode.dream_shops.request.AddProductRequest;
import com.dailycode.dream_shops.request.ProductUpdateRequest;

import java.util.List;

public interface ProductService {
    Product addproduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProduct(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String Category, String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductsByBrandAndName(String category, String name);

    Long countProductByBrandAndName(String brand, String name);


}
