package culinaryapi_Menu_Service.services.impl;

import culinaryapi_Menu_Service.dtos.ProductDto;
import culinaryapi_Menu_Service.enums.ActionType;
import culinaryapi_Menu_Service.enums.Category;
import culinaryapi_Menu_Service.models.ProductModel;
import culinaryapi_Menu_Service.publishers.MenuEventPublisher;
import culinaryapi_Menu_Service.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuEventPublisher menuEventPublisher;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private ProductDto createProductDto() {
        ProductDto dto = new ProductDto();
        dto.setName("Pizza Margherita");
        dto.setDescription("Classic Italian pizza");
        dto.setPrice(BigDecimal.valueOf(29.99));
        dto.setAvailable(true);
        dto.setCategory(Category.FOOD);
        return dto;
    }

    @Test
    void testRegisterProduct_Success() {
        ProductDto productDto = createProductDto();
        when(productRepository.existsByName(productDto.getName())).thenReturn(false);
        when(productRepository.save(any(ProductModel.class))).thenAnswer(i -> i.getArgument(0));

        ProductModel savedProduct = productService.registerProduct(productDto);

        assertNotNull(savedProduct);
        assertEquals(productDto.getName(), savedProduct.getName());
        assertTrue(savedProduct.getAvailable());
        assertNotNull(savedProduct.getCreationDate());
        verify(menuEventPublisher).publishMenuEvent(any(), eq(ActionType.CREATE));
    }

    @Test
    void testRegisterProduct_DuplicateName_ThrowsException() {
        ProductDto productDto = createProductDto();
        when(productRepository.existsByName(productDto.getName())).thenReturn(true);

        var exception = assertThrows(IllegalArgumentException.class, () ->
                productService.registerProduct(productDto)
        );

        assertEquals("Error: Product name is already taken!", exception.getMessage());
        verify(productRepository, never()).save(any());
        verify(menuEventPublisher, never()).publishMenuEvent(any(), any());
    }

    @Test
    void testUpdateProduct_Success() {
        UUID productId = UUID.randomUUID();
        ProductDto productDto = createProductDto();

        ProductModel existingProduct = new ProductModel();
        existingProduct.setProductId(productId);
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Desc");
        existingProduct.setPrice(BigDecimal.valueOf(19.99));
        existingProduct.setAvailable(false);
        existingProduct.setCategory(Category.DRINK);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));
        when(productRepository.save(any(ProductModel.class))).thenAnswer(i -> i.getArgument(0));

        ProductModel updatedProduct = productService.updateProduct(productId, productDto);

        assertNotNull(updatedProduct);
        assertEquals(productDto.getName(), updatedProduct.getName());
        assertEquals(productDto.getDescription(), updatedProduct.getDescription());
        assertEquals(productDto.getPrice(), updatedProduct.getPrice());
        assertEquals(productDto.getAvailable(), updatedProduct.getAvailable());
        assertEquals(productDto.getCategory(), updatedProduct.getCategory());
        assertNotNull(updatedProduct.getLastUpdateDate());
    }

    @Test
    void testUpdateProduct_ProductNotFound_ThrowsException() {
        UUID invalidId = UUID.randomUUID();
        ProductDto productDto = createProductDto();

        when(productRepository.findById(invalidId)).thenReturn(java.util.Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(invalidId, productDto)
        );

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

}
