package com.example.productservice.repository.custom;

import com.example.productservice.dto.ProductDetailDto;

import java.util.Optional;

public interface ProductCustomRepository {

    Optional<ProductDetailDto> findByIdJdbc(Long id);
}
