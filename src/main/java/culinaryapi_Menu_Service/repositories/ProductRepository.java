package culinaryapi_Menu_Service.repositories;

import culinaryapi_Menu_Service.enums.Category;
import culinaryapi_Menu_Service.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    boolean existsByname(String name);

    Page<ProductModel> findByCategory(Category category, Pageable pageable);
}
