package com.example.productservice.service.impl;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDetailDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import com.example.productservice.model.view.ProductView;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.view.ProductViewRepository;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_ID_NOT_FOUND_TEMPLATE = "product id %s";

    ProductRepository productRepository;
    ProductViewRepository productViewRepository;
    ModelMapper modelMapper;
    ProductMapper productMapper;

    @Override
    public ProductDetailDto findByIdJdbc(Long id) throws NotFoundException {
        return getByIdJdbc(id);
    }

    private ProductDetailDto getByIdJdbc(Long id) throws NotFoundException {
        return productRepository.findByIdJdbc(id).orElseThrow(() -> new NotFoundException(String.format(PRODUCT_ID_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    public ProductDetailDto findByIdJpql(Long id) throws NotFoundException {
        return getByIdJpql(id);
    }

    private ProductDetailDto getByIdJpql(Long id) throws NotFoundException {
        return productRepository.findByIdJpql(id).orElseThrow(() -> new NotFoundException(String.format(PRODUCT_ID_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    public ProductDetailDto findByIdView(Long id) throws NotFoundException {
        var productView = getByIdView(id);
        return modelMapper.map(productView, ProductDetailDto.class);
    }

    private ProductView getByIdView(Long id) throws NotFoundException {
        return productViewRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(PRODUCT_ID_NOT_FOUND_TEMPLATE, id)));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto findById(Long id) throws NotFoundException {
        var product = getById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    private Product getById(Long id) throws NotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(PRODUCT_ID_NOT_FOUND_TEMPLATE, id)));
    }

    @Transactional
    @Override
    public IdListResponse update(UpdateProductRequest request) throws NotFoundException {
        var product = getById(request.getId());
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
        var productList = request.getProductList().stream()
                .map(createProductRequest -> modelMapper.map(createProductRequest, Product.class)).toList();

        productRepository.saveAllAndFlush(productList);
        List<Long> idList = productList.stream().map(Product::getId).toList();
        return new IdListResponse(idList);
    }

    private PageResponse<ProductDto> findAll(Integer pageNumber, Integer pageSize, Sort sort) {
        var pageable = PageUtils.buildPageable(pageNumber, pageSize, sort);
        var page = productRepository.findAll(pageable);
        var content = page.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();
        return new PageResponse<>(content, page);
    }

    @Override
    public PageResponse<ProductDto> findAllSortMultiColumn(MultiSortPageRequest request) {
        var orderList = request.getOrderList();
        List<Sort.Order> orders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orders = orderList.stream().map(AppSortOrder::mapToSortOrder).collect(Collectors.toList());
        }
        PageUtils.addDefaultOrder(orders);
        return findAll(request.getPageNumber(), request.getPageSize(), Sort.by(orders));
    }

    @Override
    public PageResponse<ProductDto> findAll(AppPageRequest request) {
        var direction = Sort.Direction.fromString(request.getSortDirection());
        var orders = request.getSortColumn().stream()
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
        var fieldFirstElement = AppReflectionUtils.getField(CursorProductDto.class, request.getSortColumn(), cursorProductDtoList.get(0));
        var fieldLastElement = AppReflectionUtils.getField(CursorProductDto.class, request.getSortColumn(), cursorProductDtoList.get(cursorProductDtoList.size() - 1));
        return new CursorPageResponse<>(cursorProductDtoList,
                PageUtils.getEncodedCursor(String.valueOf(fieldFirstElement)),
                PageUtils.getEncodedCursor(String.valueOf(fieldLastElement)),
                productPage
        );
    }
}

