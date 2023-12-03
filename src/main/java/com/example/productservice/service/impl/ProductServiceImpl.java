package com.example.productservice.service.impl;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import com.example.springbootmicroservicesframework.config.specification.PageSpecification;
import com.example.springbootmicroservicesframework.dto.AppPageRequest;
import com.example.springbootmicroservicesframework.dto.AppSortOrder;
import com.example.springbootmicroservicesframework.dto.CursorPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageResponse;
import com.example.springbootmicroservicesframework.dto.IdListResponse;
import com.example.springbootmicroservicesframework.dto.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.dto.PageResponse;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.example.springbootmicroservicesframework.utils.AppReflectionUtils;
import com.example.springbootmicroservicesframework.utils.PageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto findById(Long id) throws NotFoundException {
        Product product = getById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    public Product getById(Long id) throws NotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("product id %s", id)));
    }

    @Transactional
    @Override
    public IdListResponse update(UpdateProductRequest request) throws NotFoundException {
        Product product = getById(request.getId());
        modelMapper.map(request, product);
        productRepository.saveAndFlush(product);
        return new IdListResponse(Collections.singletonList(product.getId()));
    }

    @Transactional
    @Override
    public IdListResponse create(CreateProductRequest request) {
        Product product = modelMapper.map(request, Product.class);

        productRepository.saveAndFlush(product);
        return new IdListResponse(Collections.singletonList(product.getId()));
    }

    @Transactional
    @Override
    public IdListResponse createMulti(CreateMultiProductRequest request) {
        List<Product> productList = request.getProductList().stream()
                .map(createProductRequest -> modelMapper.map(createProductRequest, Product.class)).toList();

        productRepository.saveAllAndFlush(productList);
        List<Long> idList = productList.stream().map(Product::getId).toList();
        return new IdListResponse(idList);
    }

    private PageResponse<ProductDto> findAll(Integer pageNumber, Integer pageSize, Sort sort) {
        Pageable pageable = PageUtils.buildPageable(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductDto> content = page.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();
        return new PageResponse<>(content, page);
    }

    @Override
    public PageResponse<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request) {
        List<AppSortOrder> orderList = request.getOrderList();
        List<Sort.Order> orders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orders = orderList.stream().map(AppSortOrder::mapToSortOrder).collect(Collectors.toList());
        }
        PageUtils.addDefaultOrder(orders);
        return findAll(request.getPageNumber(), request.getPageSize(), Sort.by(orders));
    }

    @Override
    public PageResponse<ProductDto> findAll(AppPageRequest request) {
        Sort.Direction direction = Sort.Direction.fromString(request.getSortDirection());
        List<Sort.Order> orders = request.getSortColumn().stream()
                .map(property -> new Sort.Order(direction, property))
                .collect(Collectors.toList());
        PageUtils.addDefaultOrder(orders);
        return findAll(request.getPageNumber(),
                request.getPageSize(),
                Sort.by(orders));
    }


    @Override
    public CursorPageResponse<CursorProductDto> findAllCursorPagination(CursorPageRequest request) throws IllegalAccessException {
        log.info("findAllCursorPagination");
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

