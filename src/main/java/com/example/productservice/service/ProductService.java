package com.example.productservice.service;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.springbootmicroservicesframework.config.pagination.AppPageRequest;
import com.example.springbootmicroservicesframework.config.pagination.CursorPageRequest;
import com.example.springbootmicroservicesframework.config.pagination.CursorPageResponse;
import com.example.springbootmicroservicesframework.config.pagination.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.dto.IdListResponse;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

public interface ProductService {

    IdListResponse create(CreateProductRequest request);

    PageImpl<ProductDto> findAll(Integer page, Integer size, Sort sort);

    PageImpl<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request);

    PageImpl<ProductDto> findAll(AppPageRequest request);

    CursorPageResponse<CursorProductDto> findAllCursorPagination(CursorPageRequest request) throws IllegalAccessException;

    IdListResponse createMulti(CreateMultiProductRequest request);

    IdListResponse update(UpdateProductRequest request) throws NotFoundException;

    ProductDto findById(Long id) throws NotFoundException;

    void deleteById(Long id) throws NotFoundException;
}

