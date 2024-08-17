package programar.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import programar.app.entities.Product;
import programar.app.services.ProductService;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;


    @GetMapping
    public String products(@RequestParam(name = "name", required = false) String name,
                           @RequestParam(name = "material", required = false) String material,
                           @RequestParam(name = "price", required = false) Double price,
                           @RequestParam(name = "discount", required = false) Integer discount,
                           @RequestParam(name = "tags", required = false) String tags){

        log.info("name: {}, material: {}, price: {}, tags: {}, ", name, material, price, tags);
        List<Product> productsFiltered = productService.filterProducts(name, material, price, tags, discount);
        log.info("filtrados: " + productsFiltered);
        return "productos";
    }

}
