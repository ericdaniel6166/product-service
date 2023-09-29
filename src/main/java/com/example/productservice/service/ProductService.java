package com.example.productservice.service;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductDto;
import com.example.springbootmicroservicesframework.pagination.AppPageRequest;
import com.example.springbootmicroservicesframework.pagination.MultiSortPageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

public interface ProductService {

    void create(CreateProductRequest request);

    PageImpl<ProductDto> findAll(Integer page, Integer size, Sort sort);

    PageImpl<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request);

    PageImpl<ProductDto> findAll(AppPageRequest request);
}
