package culinaryapi_Menu_Service.controllers;

import culinaryapi_Menu_Service.dtos.ProductDto;
import culinaryapi_Menu_Service.models.ProductModel;
import culinaryapi_Menu_Service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Registrar produto", description = "Registra um novo produto no sistema. Somente ADMIN pode acessar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito: Produto já existe ou dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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

    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
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

    @Operation(summary = "Listar produtos", description = "Retorna uma página com todos os produtos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ProductModel>> getAllProducts(
            @PageableDefault(page = 0, size = 10, sort = "productId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductModel> productModels = productService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(productModels);
    }
}