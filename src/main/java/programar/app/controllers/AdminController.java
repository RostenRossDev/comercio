package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import programar.app.dtos.ConfirmUpdateDeleteProductDTO;
import programar.app.dtos.ProductFilter;
import programar.app.entities.Product;
import programar.app.services.ProductService;
import programar.app.services.impl.FileServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/administracion-negocio")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileServiceImpl fileService;

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
    public ResponseEntity<String> updateProducts( @RequestBody ConfirmUpdateDeleteProductDTO payload) {
        List<Product>  updatedProducts = payload.getProducts();
        List<Long> productsToDelete = payload.getToDelete();
        // Implement your update logic here
        log.info("Productos recividos: " + updatedProducts);
        log.info("toDelete recividos: " + productsToDelete);


        List<Long> prodtsIds = updatedProducts.stream().map(item -> item.getId()).toList();
        List<Product> toUpdate = productService.findByIdIn(prodtsIds);
        log.info("Productos para actualizar: " + toUpdate);
        toUpdate.forEach(item -> {
            updatedProducts.forEach(prod -> {
                if(prod.getTag() != null && !prod.getTag().isBlank() && !prod.getTag().isEmpty()){
                    item.setTag(prod.getTag());
                }

                if(prod.getEnabled() != null){
                    item.setEnabled(prod.getEnabled());
                }

                if(prod.getSale() != null && prod.getSale() > 0 && prod.getSale() < 100){
                    item.setSale(prod.getSale());
                }

                if(prod.getRealStock() != null && prod.getRealStock() > 0){
                    item.setRealStock(prod.getRealStock());
                }

                if(prod.getName() != null && !prod.getName().isEmpty() && !prod.getName().isBlank()){
                    item.setName(prod.getName());
                }
//
//                if(prod.getImg() != null && !prod.getImg().isEmpty() &&
//                        !prod.getImg().isBlank() && !item.getImg().equals(prod.getImg()) && fileService.deleteFile(prod.getImg())){
//
//                    item.setImg(prod.getImg());
//                }
            });
        });

        log.info("Productos para actualizados: " + toUpdate);

        productService.saveAll(toUpdate);
        productService.deleteAll(productsToDelete);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
