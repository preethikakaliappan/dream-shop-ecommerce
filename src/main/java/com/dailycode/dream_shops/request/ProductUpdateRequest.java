package com.dailycode.dream_shops.request;

import com.dailycode.dream_shops.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;  // quantity
    private Category category;
}
