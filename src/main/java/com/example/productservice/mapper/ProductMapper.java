package com.example.productservice.mapper;

import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.model.Product;
import com.example.springbootmicroservicesframework.pagination.PageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    String QUALIFIED_MAP_CURSOR_ID = "mapCursorId";
    String TARGET_CURSOR_ID = "cursorId";
    String SOURCE_ID = "id";

    @Named(QUALIFIED_MAP_CURSOR_ID)
    static String mapCursorId(Long id) {
        return PageUtils.getEncodedCursor(String.valueOf(id));
    }

    @Mapping(source = SOURCE_ID, target = TARGET_CURSOR_ID, qualifiedByName = QUALIFIED_MAP_CURSOR_ID)
    CursorProductDto mapCursorProductDto(Product product, @MappingTarget CursorProductDto cursorProductDto);

}
