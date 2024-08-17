package programar.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Product;
import programar.app.services.ProductService;

import java.util.List;

@Controller
@RequestMapping("/administracion-negocio")
public class AdminController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getAdminPage(Model model) {
        // You can add initial data to the model if needed
        return "admin";
    }

    @PostMapping("/filter")
    @ResponseBody
    public List<Product> filterProducts(@RequestBody ProductFilter filter) {
        // Implement your filtering logic here
        return productService.filterProducts(filter);
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateProducts(@RequestBody List<Product> products) {
        // Implement your update logic here
        return "success";
    }

}
