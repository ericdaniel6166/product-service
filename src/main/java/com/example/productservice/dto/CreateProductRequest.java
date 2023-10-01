package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.utils.Const;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    //    @Size(max = 10) //delete //for local test
    @NotBlank
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    private String name;

    //    @Size(max = 10) //delete //for local test
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    private String description;

    @Min(value = Const.ZERO)
    @Max(value = Const.DEFAULT_MAX_NUMBER)
    private BigDecimal price;
}
