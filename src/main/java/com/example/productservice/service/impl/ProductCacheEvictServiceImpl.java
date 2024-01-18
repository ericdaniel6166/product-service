package com.example.productservice.service.impl;

import com.example.productservice.service.ProductCacheEvictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductCacheEvictServiceImpl implements ProductCacheEvictService {


    @CacheEvict(value = {"product.findById"}, key = "#id")
    @Override
    public void evictCacheProductFindById(Long id) {
        //
    }
}
