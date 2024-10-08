package programar.app.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Product;
import programar.app.repositories.ProductRepository;
import programar.app.services.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;


    @Override
    public List<Product> filterProducts(ProductFilter filter) {

        return List.of(null);
    }

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public List<Product> findAll() {

        List<Product> products = repository.findAll();
        return products;
    }

    @Override
    public List<Product> filterProducts(String name, Double price, String tags) {
        List<Product> products = repository.findAll();
        log.info("name: " + name);
        log.info("price: " + price);
        log.info("tags: " + tags);

        if( name == null && price == null && tags == null){
            log.info("prod: " + products);
            log.info("prod: " + products.size());
            return products;
        }
        List<Product> filtered = products.stream()
                .filter(product -> {
                    boolean matches = true;
                    if (name != null) {
                        String productName = product.getName().toLowerCase();
                        String searchName = name.toLowerCase();
                        if (!productName.contains(searchName)) {
                            matches = false;
                        }
                    }
                    return matches;
                }).filter(product -> {
                    boolean matches = true;
                    log.info("filtro price: " + price + ", producto: " + product);
                    if(price != null){
                        matches = product.getPrice() <= price;
                        log.info("matches: " + matches);
                    }
                    return matches;
                }).filter(product -> {
                    log.info("filtro tags: " + tags + ", producto: " + product);

                    boolean matches = true;
                    if (tags != null && product.getTag() != null) {
                        String productTag = product.getTag().toLowerCase();
                        String searchTag = tags.toLowerCase();
                        if (!productTag.contains(searchTag)) {
                            matches = false;
                        }
                    }
                    return matches;
                }).collect(Collectors.toList());
        log.info("############################ filtrados ######################");
        for (Product product : filtered) {
            log.info("######### " + product);
        }
        return filtered;
    }

    @Override
    @Transactional
    public ResponseEntity<Product> updateStock(Long productId, int quantityToAdd){
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        log.info("Hay stock: " + (product.getStock() > (product.getStock() - quantityToAdd)));
        log.info("quantityToAdd: " +quantityToAdd);
        // Verifica si hay suficiente stock
        if(quantityToAdd < 0){
            log.info("el stock real es mayor al que se va a mostrar ?: " + ((product.getStock() - quantityToAdd) <= product.getRealStock()));

            if ((product.getStock() - quantityToAdd) <= product.getRealStock()) {
                log.info("stock real: " + product.getRealStock());
                log.info("stock actualizado a mostrar: " + ((product.getStock() - quantityToAdd)));
                log.info("product: " + product);
                product.setStock(product.getStock() - quantityToAdd);
                Product updateProd=repository.save(product);
                return new ResponseEntity<>(updateProd, HttpStatus.OK);
            }
            return new ResponseEntity<>(product, HttpStatus.OK);
        }else{
            if ((product.getStock() - quantityToAdd) >= 0) {
                log.info("Hay stock: " + (product.getStock() > (product.getStock() - quantityToAdd)));
                log.info("stock: " + product.getStock());
                log.info("stock actualizado al agregar stock: " + ((product.getStock() - quantityToAdd)));
                log.info("product: " + product);
                product.setStock(product.getStock() - quantityToAdd);
                Product updateProd=repository.save(product);
                return new ResponseEntity<>(updateProd, HttpStatus.OK);
            } else {
                log.info("No hay stock");
                log.info("product: " + product);
                return new ResponseEntity<>(product, HttpStatus.OK);
            }
        }
    }

    @Override
    public List<Product> ofertas() {
        List<Product> ofertas  = repository.findByEnabledTrueAndSaleIsNotNull();
        log.info("ofertas: " + ofertas);
        ofertas = ofertas.stream().filter(item -> item.getSale() != 0).collect(Collectors.toList());
        log.info("ofertas filtradas: " + ofertas);
        return ofertas;
    }

    @Override
    public List<Product> productosHabilitados() {
        return repository.findByEnabledTrueAndSale(0);
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findByIdIn(List<Long> ids) {
        return repository.findByIdIn(ids);
    }

    @Override
    public List<Product> saveAll(List<Product> toUpdate) {
        return repository.saveAll(toUpdate);
    }

    @Override
    public void deleteAll(List<Long> toDelete) {
        repository.deleteAllById(toDelete);
    }
}
