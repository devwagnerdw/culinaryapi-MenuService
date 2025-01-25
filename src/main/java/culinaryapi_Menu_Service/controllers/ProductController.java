package culinaryapi_Menu_Service.controllers;

import culinaryapi_Menu_Service.dtos.ProductDto;
import culinaryapi_Menu_Service.models.ProductModel;
import culinaryapi_Menu_Service.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object>registerProduct( @RequestBody  ProductDto productDto){

        if (productService.existsByname(productDto.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDto,productModel);
        productModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        productModel.setAvailable(true);
        productService.save(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productModel);
    };

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id")UUID id,
                                                      @RequestBody @Valid ProductDto productDto){
        Optional<ProductModel> optionalProductModel= productService.findBayId(id);
        if (optionalProductModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USUARIO NÃO ENCONCTRADO");
        }
        var productModel = optionalProductModel.get();

        productModel.setName(productDto.getName());
        productModel.setDescription(productDto.getDescription());
        productModel.setPrice(productDto.getPrice());
        productModel.setAvailable(productDto.getAvailable());
        productModel.setCategory(productDto.getCategory());
        productModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        productService.save(productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productModel);

    }


    @GetMapping
    public ResponseEntity<Page<ProductModel>> getAllProducts(@PageableDefault(page = 0, size = 10, sort = "Id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductModel> productModels = productService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(productModels);
    }


}

