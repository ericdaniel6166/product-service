package com.example.productservice.api;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.service.ProductService;
import com.example.springbootmicroservicesframework.dto.AppPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageResponse;
import com.example.springbootmicroservicesframework.dto.IdListResponse;
import com.example.springbootmicroservicesframework.dto.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.example.springbootmicroservicesframework.utils.Const;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@Validated
public class ProductApi {
    final ProductService productService;

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteById(@PathVariable @NotNull @Min(value = 1)
                                                 @Max(value = Const.DEFAULT_MAX_LONG) Long id)
            throws NotFoundException {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable @NotNull @Min(value = 1)
                                               @Max(value = Const.DEFAULT_MAX_LONG) Long id)
            throws NotFoundException {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping
    public ResponseEntity<IdListResponse> update(@RequestBody @Valid UpdateProductRequest request) throws NotFoundException {
        return ResponseEntity.ok(productService.update(request));
    }


    @PostMapping
    public ResponseEntity<IdListResponse> create(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }


    @PostMapping("/create-multi")
    public ResponseEntity<IdListResponse> createMulti(@RequestBody @Valid CreateMultiProductRequest request) {
        return ResponseEntity.ok(productService.createMulti(request));
    }

    @GetMapping
    public ResponseEntity<PageImpl<ProductDto>> findAll(@Valid AppPageRequest request) {
        log.info("findAll");
        PageImpl<ProductDto> response = productService.findAll(request);
        if (!response.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sort-multi-column")
    public ResponseEntity<PageImpl<ProductDto>> findAllSortMultiColumn(@RequestBody @Valid MultiSortPageRequest request) {
        log.info("findAllSortMultiColumn");
        PageImpl<ProductDto> response = productService.findAllSortMultiColumn(request);
        if (!response.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cursor-pagination")
    public ResponseEntity<CursorPageResponse<CursorProductDto>> findAllCursorPagination(@Valid CursorPageRequest request)
            throws IllegalAccessException {
        log.info("findAllCursorPagination");
        CursorPageResponse<CursorProductDto> response = productService.findAllCursorPagination(request);
        if (CollectionUtils.isEmpty(response.getContent())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

}
