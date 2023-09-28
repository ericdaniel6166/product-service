package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    void create(ProductRequest productRequest);

    List<ProductResponse> getProducts();
}
