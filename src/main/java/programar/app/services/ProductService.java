package programar.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Product;

import java.util.List;

@Service
public interface ProductService {
    List<Product> filterProducts(ProductFilter filter);

    List<Product> filterProducts(String name, String material, Double price, String tags, Integer discount);

    ResponseEntity<?> updateStock(Long productId, int quantityToAdd)  throws  Exception;

    List<Product> ofertas();

    List<Product> productosHabilitados();

    Product findById(Long id);

    List<Product> findByIdIn(List<Long> ids);

    List<Product> saveAll(List<Product> toUpdate);

    void deleteAll(List<Long> toDelete);
}
