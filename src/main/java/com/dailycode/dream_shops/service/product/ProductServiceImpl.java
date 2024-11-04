package com.dailycode.dream_shops.service.product;

import com.dailycode.dream_shops.Exception.ProductNotFoundException;
import com.dailycode.dream_shops.Exception.ResourceNotFoundException;
import com.dailycode.dream_shops.model.Category;
import com.dailycode.dream_shops.model.CategoryRepository;
import com.dailycode.dream_shops.model.Product;
import com.dailycode.dream_shops.model.ProductRepository;
import com.dailycode.dream_shops.request.AddProductRequest;
import com.dailycode.dream_shops.request.ProductUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;




    @Override
    public Product addproduct(AddProductRequest request) {
        // check if the category is found in the DB
        // is  Yes, set it as the new product category
        // if No, the save it as a new category
        // The set as the new product category.

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName())).orElseGet(()-> {
            Category newCategory = new Category(request.getCategory().getName());
            return  categoryRepository.save(newCategory);

        });
        request.setCategory(category);
        return  productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category

        );

    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {

        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String Category, String brand) {
        return productRepository.findByCategoryNameAndBrand(Category, brand);
    }


    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundException("Product not found!");
        });

    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId).map(exisitingProduct -> updateExisitingProduct(exisitingProduct, request)).map(productRepository::save).orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

    }

    private Product updateExisitingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList;
    }






}

