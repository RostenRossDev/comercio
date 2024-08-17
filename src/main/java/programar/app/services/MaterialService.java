package programar.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Material;
import programar.app.entities.Product;

import java.util.List;

@Service
public interface MaterialService {

    Material addMaterial(Material material);

    List<Material> findAll();

    void delete(Long id);
}
