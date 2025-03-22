package culinaryapi_Menu_Service.controllers;

import culinaryapi_Menu_Service.dtos.ProductDto;
import culinaryapi_Menu_Service.models.ProductModel;
import culinaryapi_Menu_Service.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> registerProduct(@RequestBody @Valid ProductDto productDto) {
        try {
            ProductModel productModel = productService.registerProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductDto productDto) {
        try {
            ProductModel productModel = productService.updateProduct(id, productDto);
            return ResponseEntity.status(HttpStatus.OK).body(productModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<ProductModel>> getAllProducts(@PageableDefault(page = 0, size = 10, sort = "productId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductModel> productModels = productService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(productModels);
    }
}