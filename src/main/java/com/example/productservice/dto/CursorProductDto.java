package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CursorProductDto extends BaseEntity<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    String cursorId;
    //    @JsonIgnore //uncomment
    Long id;
    String name;
    String description;
    BigDecimal price;


}
