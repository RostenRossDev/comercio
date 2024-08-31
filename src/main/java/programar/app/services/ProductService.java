package programar.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Product;

import java.util.List;

@Service
public interface ProductService {

    List<Product> findAll();

    List<Product> filterProducts(ProductFilter filter);

    List<Product> filterProducts(String name, Double price, String tags);

    ResponseEntity<?> updateStock(Long productId, int quantityToAdd)  throws  Exception;

    List<Product> ofertas();

    List<Product> productosHabilitados();

    Product findById(Long id);

    List<Product> findByIdIn(List<Long> ids);

    List<Product> saveAll(List<Product> toUpdate);

    void deleteAll(List<Long> toDelete);
}
