package com.example.productservice.service;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.springbootmicroservicesframework.dto.AppPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageResponse;
import com.example.springbootmicroservicesframework.dto.IdListResponse;
import com.example.springbootmicroservicesframework.dto.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.dto.PageResponse;
import com.example.springbootmicroservicesframework.exception.NotFoundException;

public interface ProductService {

    IdListResponse create(CreateProductRequest request);

    PageResponse<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request);

    PageResponse<ProductDto> findAll(AppPageRequest request);

    CursorPageResponse<CursorProductDto> findAllCursorPagination(CursorPageRequest request) throws IllegalAccessException;

    IdListResponse createMulti(CreateMultiProductRequest request);

    IdListResponse update(UpdateProductRequest request) throws NotFoundException;

    ProductDto findById(Long id) throws NotFoundException;

    void deleteById(Long id);
}

