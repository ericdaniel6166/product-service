package com.example.productservice.api;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.service.ProductService;
import com.example.springbootmicroservicesframework.pagination.AppPageRequest;
import com.example.springbootmicroservicesframework.pagination.MultiSortPageRequest;
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

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ProductApi {
    final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody CreateProductRequest request) {
        productService.create(request);
    }

    @GetMapping
    public ResponseEntity<PageImpl<ProductDto>> findAll(@Valid AppPageRequest request) {
        System.out.println(request.toString()); //delete
        log.info("findAll"); //delete
        PageImpl<ProductDto> response = productService.findAll(request);
        if (!response.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sort-multi-column")
    public ResponseEntity<PageImpl<ProductDto>> findAllSortMultiColumn(@RequestBody @Valid MultiSortPageRequest request) {
        System.out.println(request.toString()); //delete
        log.info("findAllSortMultiColumn"); //delete

        PageImpl<ProductDto> response = productService.findAllSortMultiColumn(request);
        if (!response.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


}
