package culinaryapi_Menu_Service.services.impl;

import culinaryapi_Menu_Service.dtos.ProductDto;
import culinaryapi_Menu_Service.enums.ActionType;
import culinaryapi_Menu_Service.exception.ConflictException;
import culinaryapi_Menu_Service.exception.NotFoundException;
import culinaryapi_Menu_Service.models.ProductModel;
import culinaryapi_Menu_Service.publishers.MenuEventPublisher;
import culinaryapi_Menu_Service.repositories.ProductRepository;
import culinaryapi_Menu_Service.services.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MenuEventPublisher menuEventPublisher;

    public ProductServiceImpl(ProductRepository productRepository, MenuEventPublisher menuEventPublisher) {
        this.productRepository = productRepository;
        this.menuEventPublisher = menuEventPublisher;
    }

    @Override
    public ProductModel registerProduct(ProductDto productDto) {


        if (productRepository.existsByName(productDto.getName())) {
            throw new ConflictException("Error: Product name is already taken!");
        }
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        productModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        productModel.setAvailable(true);
        productRepository.save(productModel);

        var menuEvent = productModel.convertToMenuEventDto();
        menuEventPublisher.publishMenuEvent(menuEvent, ActionType.CREATE);

        return productModel;
    }

    @Override
    public ProductModel updateProduct(UUID id, ProductDto productDto) {
        ProductModel productModel = productRepository.findById(id)
                .orElseThrow(()->new NotFoundException(("product not found")));
        productModel.setName(productDto.getName());
        productModel.setDescription(productDto.getDescription());
        productModel.setPrice(productDto.getPrice());
        productModel.setAvailable(productDto.getAvailable());
        productModel.setCategory(productDto.getCategory());
        productModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        productRepository.save(productModel);

        var menuEvent= productModel.convertToMenuEventDto();
        menuEventPublisher.publishMenuEvent(menuEvent,ActionType.UPDATE);
        return productModel;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID id) {
        ProductModel productModel = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        menuEventPublisher.publishMenuEvent(productModel.convertToMenuEventDto(),ActionType.DELETE);
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
