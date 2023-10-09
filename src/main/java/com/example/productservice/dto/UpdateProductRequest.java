package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.utils.Const;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
