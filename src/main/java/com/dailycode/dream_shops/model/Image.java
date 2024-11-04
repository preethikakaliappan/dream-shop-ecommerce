package com.dailycode.dream_shops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )

    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;

    @Lob   //LOB or Large Object refers to a variable-length datatype for storing large objects.
    private Blob image;
    private String downloadUrl;


    @ManyToOne       //many images belong to one product
    @JoinColumn(name="product_id")   //foreign key connecting two tables
    private Product product;

}
