package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import programar.app.entities.Product;
import programar.app.services.ProductService;

import java.util.List;

@Log4j2
@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/cart/add")
    public ResponseEntity<Product> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        log.info("ProductRestController.addToCart");
        log.info("productId: " + productId+ ", quantity: " + quantity);
        try {
            Product prod = (Product) productService.updateStock(productId, quantity).getBody();
            return ResponseEntity.ok(prod);
        } catch (OptimisticLockingFailureException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Product());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Product());
        }
    }

    @GetMapping("/filtrar")
    public  ResponseEntity<List<Product>> filter(@RequestParam(name = "name", required = false) String name,
                           @RequestParam(name = "price", required = false) Double price,
                           @RequestParam(name = "tags", required = false) String tags,
                           Model model){

        List<Product> productsFiltered;
        if ((name != null && !name.isEmpty()) || price != null || (tags != null && !tags.isEmpty())) {
            productsFiltered  = productService.filterProducts(name, price, tags);
        }else {
            productsFiltered =productService.findAll();
        }

        log.info("filtrados: " + productsFiltered);

        return ResponseEntity.ok(productsFiltered);
    }
}
