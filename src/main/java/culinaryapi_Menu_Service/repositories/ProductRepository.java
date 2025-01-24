package culinaryapi_Menu_Service.repositories;

import culinaryapi_Menu_Service.enums.Category;
import culinaryapi_Menu_Service.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    boolean existsByname(String name);

    boolean existsByCategory(Category category);
}
