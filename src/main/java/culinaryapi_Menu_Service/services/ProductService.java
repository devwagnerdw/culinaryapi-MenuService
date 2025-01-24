package culinaryapi_Menu_Service.services;


import culinaryapi_Menu_Service.enums.Category;
import culinaryapi_Menu_Service.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    boolean existsByname(String name);

    void save(ProductModel productModel);

    Page<ProductModel> findAll(Pageable pageable);

    boolean existsByCategory(Category category);
}
