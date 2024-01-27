package com.example.productservice.service.impl;

import com.example.productservice.service.ProductCacheEvictService;
import com.example.productservice.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductCacheEvictServiceImpl implements ProductCacheEvictService {


    @CacheEvict(value = {Constants.CACHE_NAME_PRODUCT_FIND_BY_ID}, key = "#id")
    @Override
    public void evictCacheProductFindById(Long id) {
        //
    }
}
