package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursorProductDto extends BaseEntity<String> {
    private String cursorId;
    //    @JsonIgnore //uncomment
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;


}
