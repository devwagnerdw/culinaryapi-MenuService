package culinaryapi_Menu_Service.services;

import culinaryapi_Menu_Service.dtos.ProductDto;
import culinaryapi_Menu_Service.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductModel registerProduct(ProductDto productDto);

    ProductModel updateProduct(UUID id, ProductDto productDto);

    Page<ProductModel> findAll(Pageable pageable);
}