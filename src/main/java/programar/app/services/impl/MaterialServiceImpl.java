package programar.app.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Material;
import programar.app.entities.Product;
import programar.app.repositories.MaterialRepository;
import programar.app.repositories.ProductRepository;
import programar.app.services.MaterialService;
import programar.app.services.ProductService;

import java.util.List;

@Log4j2
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository repository;


    @Override
    public Material addMaterial(Material material) {
        return null;
    }

    @Override
    public List<Material> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {

    }
}
