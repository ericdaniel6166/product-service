package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.utils.Const;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {

    @NotNull
    @Min(value = 1)
    Long id;

    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    String name;

    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    String description;

    @Digits(integer = Const.MAXIMUM_BIG_DECIMAL_INTEGER, fraction = Const.MAXIMUM_BIG_DECIMAL_FRACTION)
    @Positive
    BigDecimal price;
}
