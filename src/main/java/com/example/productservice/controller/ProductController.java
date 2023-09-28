package com.example.productservice.controller;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.service.ProductService;
import com.example.springbootmicroservicesframework.dto.AppPageRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ProductController {
    final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.create(productRequest);
    }

    @GetMapping
    public ResponseEntity<PageImpl<ProductResponse>> getProducts(@Valid AppPageRequest request) {
        System.out.println(request.toString()); //delete
        log.info("getProducts"); //delete
        PageImpl<ProductResponse> productResponses = new PageImpl<>(Collections.singletonList(new ProductResponse()));
        return ResponseEntity.ok(productResponses);
//        return productService.getProducts(request);
    }
}
