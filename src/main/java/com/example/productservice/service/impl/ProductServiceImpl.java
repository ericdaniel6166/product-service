package com.example.productservice.service.impl;

import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import com.example.springbootmicroservicesframework.pagination.AppPageRequest;
import com.example.springbootmicroservicesframework.pagination.AppSortOrder;
import com.example.springbootmicroservicesframework.pagination.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.utils.Const;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;

    final ModelMapper modelMapper;

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
    public PageImpl<ProductDto> findAll(Integer page, Integer size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> result = productRepository.findAll(pageable);
        List<ProductDto> productDto = result.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();
        return new PageImpl<>(productDto,
                PageRequest.of(page + 1, size, sort),
                result.getTotalElements());
    }

    @Override
    public PageImpl<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request) {
        List<AppSortOrder> orderList = request.getOrderList();
        List<Sort.Order> orders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orders = orderList.stream().map(AppSortOrder::mapToSortOrder).toList();
        }
        Sort.Order defaultOrder = new Sort.Order(Sort.Direction.ASC, Const.ID);
        orders.add(defaultOrder);
        return findAll(request.getPageNumber() - 1, request.getPageSize(), Sort.by(orders));
    }

    @Override
    public PageImpl<ProductDto> findAll(AppPageRequest request) {
        return findAll(request.getPageNumber() - 1,
                request.getPageSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDirection()),
                        request.getSortColumn()));
    }
}

