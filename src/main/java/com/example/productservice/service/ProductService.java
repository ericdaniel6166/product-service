package com.example.productservice.service;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.springbootmicroservicesframework.dto.IdListResponse;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.example.springbootmicroservicesframework.pagination.AppPageRequest;
import com.example.springbootmicroservicesframework.pagination.CursorPageRequest;
import com.example.springbootmicroservicesframework.pagination.CursorPageResponse;
import com.example.springbootmicroservicesframework.pagination.MultiSortPageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

public interface ProductService {

    IdListResponse saveAndFlush(CreateProductRequest request);

    PageImpl<ProductDto> findAll(Integer page, Integer size, Sort sort);

    PageImpl<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request);

    PageImpl<ProductDto> findAll(AppPageRequest request);

    CursorPageResponse<CursorProductDto> findAllCursorPagination(CursorPageRequest request) throws IllegalAccessException;

    IdListResponse saveAllAndFlush(CreateMultiProductRequest request);

    IdListResponse update(UpdateProductRequest request) throws NotFoundException;

    ProductDto getById(Long id) throws NotFoundException;
}
