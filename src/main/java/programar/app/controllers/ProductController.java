package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import programar.app.entities.Product;
import programar.app.services.ProductService;
import programar.app.services.impl.FileServiceImpl;

import java.util.List;

@Log4j2

@Controller
@RequestMapping("/productos")
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private FileServiceImpl fileService;

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

    @GetMapping("/update/{productId}")//ruta para mostrar el formulario
    public String products(@PathVariable(value = "productId") Long productId, Model model){
        Product prod = productService.findById(productId);
        model.addAttribute("product", prod);
        return "form";
    }

    @PostMapping("/form")
    public String guardar(Model model,  @RequestBody Product prod) {
        if(prod == null){

            return "redirect:/administracion-negocio";
        }

        Product product = productService.findById(prod.getId());
        if(product == null){

            return "redirect:/administracion-negocio";
        }

        if(prod.getTag() != null && !prod.getTag().isBlank() && !prod.getTag().isEmpty()){
            product.setTag(prod.getTag());
        }

        if(prod.getEnabled() != null){
            product.setEnabled(prod.getEnabled());
        }

        if(prod.getSale() != null && prod.getSale() > 0 && prod.getSale() < 100){
            product.setSale(prod.getSale());
        }

        if(prod.getRealStock() != null && prod.getRealStock() > 0){
            product.setRealStock(prod.getRealStock());
        }

        if(prod.getName() != null && !prod.getName().isEmpty() && !prod.getName().isBlank()){
            product.setName(prod.getName());
        }

        if(prod.getImg() != null && !prod.getImg().isEmpty() &&
                !prod.getImg().isBlank() && !product.getImg().equals(prod.getImg()) && fileService.deleteFile(prod.getImg())){

            product.setImg(prod.getImg());
        }

        return "redirect:/administracion-negocio";
    }
}
