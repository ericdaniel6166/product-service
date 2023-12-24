package com.example.productservice.repository;

import com.example.productservice.dto.ProductDetailDto;
import com.example.productservice.model.Product;
import com.example.productservice.repository.custom.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, ProductCustomRepository {
    Page<Product> findAll(Pageable pageable);

    @Query("""
            select new com.example.productservice.dto.ProductDetailDto(t1.createdBy, t1.createdDate, t1.lastModifiedBy, 
            t1.lastModifiedDate, t1.id, t1.name, t1.description, t1.price, t2.name) 
            from Product t1 join Category t2 
            on t1.categoryId = t2.id 
            where t1.id = :id
            """)
    Optional<ProductDetailDto> findByIdJpql(Long id);

}
