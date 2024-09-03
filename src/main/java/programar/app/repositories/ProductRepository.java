package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Altura;
import programar.app.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByEnabledTrueAndSaleIsNotNull();

    List<Product> findByEnabledTrue();

    List<Product> findByEnabledTrueAndSale(int sale);

    List<Product> findByIdIn(List<Long> ids);

}
