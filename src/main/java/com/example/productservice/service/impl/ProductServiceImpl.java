package com.example.productservice.service.impl;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import com.example.springbootmicroservicesframework.pagination.AppPageRequest;
import com.example.springbootmicroservicesframework.pagination.AppSortOrder;
import com.example.springbootmicroservicesframework.pagination.CursorPageRequest;
import com.example.springbootmicroservicesframework.pagination.CursorPageResponse;
import com.example.springbootmicroservicesframework.pagination.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.pagination.PageSpecification;
import com.example.springbootmicroservicesframework.pagination.PageUtils;
import com.example.springbootmicroservicesframework.utils.AppReflectionUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;

    final ModelMapper modelMapper;

    final ProductMapper productMapper;

    @Transactional
    @Override
    public void create(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        productRepository.saveAndFlush(product);
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public PageImpl<ProductDto> findAll(Integer pageNumber, Integer pageSize, Sort sort) {
        Pageable pageable = PageUtils.buildPageable(pageNumber, pageSize, sort);
        Page<Product> result = productRepository.findAll(pageable);
        List<ProductDto> productDtoList = result.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();
        return PageUtils.buildAppPageImpl(pageNumber, pageSize, sort, result.getTotalElements(), productDtoList);
    }

    @Override
    public PageImpl<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request) {
        List<AppSortOrder> orderList = request.getOrderList();
        List<Sort.Order> orders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orders = orderList.stream().map(AppSortOrder::mapToSortOrder).collect(Collectors.toList());
        }
        PageUtils.addDefaultOrder(orders);
        return findAll(request.getPageNumber(), request.getPageSize(), Sort.by(orders));
    }

    @Override
    public PageImpl<ProductDto> findAll(AppPageRequest request) {
        return findAll(request.getPageNumber(),
                request.getPageSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDirection()),
                        request.getSortColumn()));
    }

    @Override
    public CursorPageResponse<CursorProductDto> findAllCursorPagination(CursorPageRequest request) throws IllegalAccessException {
        var specification = new PageSpecification<Product>(request);
        var productPage = productRepository.findAll(specification, Pageable.ofSize(request.getPageSize()));
        if (CollectionUtils.isEmpty(productPage.getContent())) {
            return PageUtils.buildBlankCursorPageResponse(productPage);
        }
        var cursorProductDtoList = productPage.getContent().stream().map(product -> productMapper.mapCursorProductDto(product, new CursorProductDto())).toList();
        Object fieldFirstElement = AppReflectionUtils.getField(CursorProductDto.class, request.getSortColumn(), cursorProductDtoList.get(0));
        Object fieldLastElement = AppReflectionUtils.getField(CursorProductDto.class, request.getSortColumn(), cursorProductDtoList.get(cursorProductDtoList.size() - 1));
        return new CursorPageResponse<>(cursorProductDtoList,
                PageUtils.getEncodedCursor(String.valueOf(fieldFirstElement)),
                PageUtils.getEncodedCursor(String.valueOf(fieldLastElement)),
                productPage
        );
    }
}

