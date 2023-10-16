package com.example.productservice.dto;

import com.example.springbootmicroservicesframework.utils.Const;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMultiProductRequest {

    //    @Size(min = 1, max = 2) //delete //local test
    @Size(min = 1, max = Const.MAXIMUM_CREATE_UPDATE_MULTI_ITEM)
    @Valid
    List<CreateProductRequest> productList;

}
