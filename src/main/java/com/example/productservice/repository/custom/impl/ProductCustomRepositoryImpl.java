package com.example.productservice.repository.custom.impl;

import com.example.productservice.dto.ProductDetailDto;
import com.example.productservice.repository.custom.ProductCustomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private static final String PARAM_NAME_ID = "id";

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<ProductDetailDto> findByIdJdbc(Long id) {
        String sql = """
                SELECT T1.CREATED_BY,
                       T1.CREATED_DATE,
                       T1.LAST_MODIFIED_BY,
                       T1.LAST_MODIFIED_DATE,
                       T1.ID,
                       T1.NAME,
                       T1.DESCRIPTION,
                       T1.PRICE,
                       T2.NAME AS CATEGORY_NAME
                FROM PRODUCT T1
                         JOIN CATEGORY T2 ON T1.CATEGORY_ID = T2.ID
                WHERE T1.ID = :id
                """;
        MapSqlParameterSource mapSql = new MapSqlParameterSource();
        mapSql.addValue(PARAM_NAME_ID, id);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, mapSql, BeanPropertyRowMapper.newInstance(ProductDetailDto.class)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
