package com.example.productservice.api;

import com.example.productservice.dto.CreateMultiProductRequest;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.CursorProductDto;
import com.example.productservice.dto.ProductDetailDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.service.ProductService;
import com.example.productservice.utils.Constants;
import com.example.springbootmicroservicesframework.dto.AppPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageRequest;
import com.example.springbootmicroservicesframework.dto.CursorPageResponse;
import com.example.springbootmicroservicesframework.dto.IdListResponse;
import com.example.springbootmicroservicesframework.dto.MultiSortPageRequest;
import com.example.springbootmicroservicesframework.dto.PageResponse;
import com.example.springbootmicroservicesframework.exception.AppNotFoundException;
import com.example.springbootmicroservicesframework.utils.Const;
import com.example.springbootmicroservicesframework.validation.ValidEnumString;
import com.example.springbootmicroservicesframework.validation.ValidString;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductApi {

    ProductService productService;
    HttpServletRequest httpServletRequest;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok("test");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/test2")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> test2() {
        return ResponseEntity.ok("test2");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteById(@PathVariable @NotNull @Min(value = 1)
                                                 @Max(value = Const.DEFAULT_MAX_LONG) Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable @NotNull @Min(value = 1)
                                               @Max(value = Const.DEFAULT_MAX_LONG) Long id)
            throws AppNotFoundException {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view/{id}")
    public ResponseEntity<ProductDetailDto> findByIdView(@PathVariable @NotNull @Min(value = 1)
                                               @Max(value = Const.DEFAULT_MAX_LONG) Long id) throws AppNotFoundException {
        return ResponseEntity.ok(productService.findByIdView(id));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/jpql/{id}")
    public ResponseEntity<ProductDetailDto> findByIdJpql(@PathVariable @NotNull @Min(value = 1)
                                               @Max(value = Const.DEFAULT_MAX_LONG) Long id) throws AppNotFoundException {
        return ResponseEntity.ok(productService.findByIdJpql(id));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/jdbc/{id}")
    public ResponseEntity<ProductDetailDto> findByIdJdbc(@PathVariable @NotNull @Min(value = 1)
                                               @Max(value = Const.DEFAULT_MAX_LONG) Long id) throws AppNotFoundException {
        return ResponseEntity.ok(productService.findByIdJdbc(id));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    public ResponseEntity<IdListResponse> update(@RequestBody @Valid UpdateProductRequest request) throws AppNotFoundException {
        return ResponseEntity.ok(productService.update(request));
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<IdListResponse> create(@RequestBody @Valid CreateProductRequest request) {
        return new ResponseEntity<>(productService.create(request), HttpStatus.CREATED);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/create-multi")
    public ResponseEntity<IdListResponse> createMulti(@RequestBody @Valid CreateMultiProductRequest request) {
        return new ResponseEntity<>(productService.createMulti(request), HttpStatus.CREATED);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> findAll(
            @RequestParam(required = false, defaultValue = Const.STRING_ONE)
            @Min(value = Const.INTEGER_ONE)
            @Max(value = Const.DEFAULT_MAX_INTEGER) Integer pageNumber,
            @RequestParam(required = false, defaultValue = Const.DEFAULT_PAGE_SIZE_STRING)
            @Min(value = Const.DEFAULT_PAGE_SIZE)
            @Max(value = Const.MAXIMUM_PAGE_SIZE) Integer pageSize,
            @RequestParam(required = false, defaultValue = Const.DEFAULT_SORT_COLUMN)
            @ValidString(values = {
                    Constants.SORT_COLUMN_ID,
                    Constants.SORT_COLUMN_NAME,
                    Constants.SORT_COLUMN_DESCRIPTION,
                    Constants.SORT_COLUMN_PRICE,
                    Constants.SORT_COLUMN_CATEGORY_ID,
            })
            @Size(min = Const.INTEGER_ONE, max = Const.MAXIMUM_SORT_COLUMN) Set<String> sortColumn,
            @RequestParam(required = false, defaultValue = Const.DEFAULT_SORT_DIRECTION)
            @ValidEnumString(value = Sort.Direction.class, caseSensitive = false) String sortDirection) {
        log.info("findAll");

        var request = new AppPageRequest(pageNumber, pageSize, sortColumn, sortDirection);
        var response = productService.findAll(request);
        if (!response.getPageable().isHasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/sort-multi-column")
    public ResponseEntity<PageResponse<ProductDto>> findAllSortMultiColumn(@RequestBody @Valid MultiSortPageRequest request) {
        log.info("findAllSortMultiColumn");
        var response = productService.findAllSortMultiColumn(request);
        if (!response.getPageable().isHasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/cursor-pagination")
    public ResponseEntity<CursorPageResponse<CursorProductDto>> findAllCursorPagination(@Valid CursorPageRequest request)
            throws IllegalAccessException {
        log.info("findAllCursorPagination");
        CursorPageResponse<CursorProductDto> response = productService.findAllCursorPagination(request);
        if (CollectionUtils.isEmpty(response.getContent())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

}
