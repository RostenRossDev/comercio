package programar.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import programar.app.dtos.CartItem;
import programar.app.entities.Product;
import programar.app.services.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
public class CarritoController {
    @Autowired
    private ProductService productService;

    @PostMapping ("/detalle-de-Compra")
    public String viewCart(@RequestParam(required = false) String  cart, Model model) {
        log.info("cart: " + cart);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CartItem> cartItems = objectMapper.readValue(cart, objectMapper.getTypeFactory().constructCollectionType(List.class, CartItem.class));
            List<CartItem> uniqueCartItems = cartItems.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(CartItem::getId, item -> item, (item1, item2) -> item1),
                            map -> new ArrayList<>(map.values())
                    ));

            List<CartItem> cartProduct = new ArrayList<>();

            uniqueCartItems.forEach(item -> {
                Product produc = productService.findById(item.getId());
                cartProduct.add( CartItem.builder()
                        .stock(produc.getStock())
                        .id(produc.getId())
                        .name(produc.getName())
                        .price(produc.getPrice().toString())
                        .discount(produc.getSale()).build());
            });

            for (int i = 0; i < uniqueCartItems.size(); i++) {
                cartProduct.get(i).setQuantity(cartProduct.get(i).getQuantity());
            }

            log.info("cartItems: " + cartProduct);
            model.addAttribute("cartItems", cartProduct);
            model.addAttribute("total", calculateTotal(cartItems));
        }catch (IOException e ){
            e.printStackTrace();
        }
        return "carrito";
    }

   /* @PostMapping("/updateCartItem")
    @ResponseBody
    public ResponseEntity<List<CartItem>> updateCartItem(@RequestParam String id, @RequestParam int quantity) {
        log.info("updateCartItem");
        cart.removeIf(item -> item.getId().equals(id));
        cart.forEach(item -> {
            if (item.getId().equals(id)) {
                item.setQuantity(quantity);
            }
        });
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
*/

  /*  @PostMapping("/removeCartItem")
    @ResponseBody
    public ResponseEntity<List<CartItem>> removeCartItem(@RequestParam String id) {
        log.info("removeCartItem");
        cart.removeIf(item -> item.getId().equals(id));
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
*/



    private double calculateTotal( List<CartItem> cart) {
        return cart.stream()
                .mapToDouble(item -> Double.parseDouble(item.getPrice()) * (1 - item.getDiscount() / 100) * item.getQuantity())
                .sum();
    }

}
