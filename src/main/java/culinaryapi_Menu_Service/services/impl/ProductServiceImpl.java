package culinaryapi_Menu_Service.services.impl;

import culinaryapi_Menu_Service.enums.Category;
import culinaryapi_Menu_Service.models.ProductModel;
import culinaryapi_Menu_Service.repositories.ProductRepository;
import culinaryapi_Menu_Service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public boolean existsByname(String name) {
        return productRepository.existsByname(name);
    }

    @Override
    public void save(ProductModel productModel) {
        productRepository.save(productModel);
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);

    }

    @Override
    public boolean existsByCategory(Category category) {
        return productRepository.existsByCategory(category);
    }
}
