package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.utils.Const;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    //    @Size(max = 10) //delete //for local test
    @NotBlank
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    private String name;

    //    @Size(max = 10) //delete //for local test
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    private String description;

    //    @Max(value = Const.DEFAULT_MAX_NUMBER)
    @Digits(integer = Const.MAXIMUM_BIG_DECIMAL_INTEGER,
            fraction = Const.MAXIMUM_BIG_DECIMAL_FRACTION)
    @Positive
    @NotNull
    private BigDecimal price;
}