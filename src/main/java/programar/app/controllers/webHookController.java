package programar.app.controllers;


import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentItem;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import programar.app.feingClient.MercadoPagoClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/webhooks")
public class webHookController {

    @Autowired
    private MercadoPagoClient mercadoPagoClient;

    @Value("${codigo.mercadoLibre}")
    private String mercadolibreToken;

    @PostMapping("/notification")
    public ResponseEntity<String> notification(@RequestBody Map<String, Object> payload){
        try {
            log.info("Payment status: " + payload);

            for (String key : payload.keySet()) {
                log.info(key + " = " + payload.get(key));
            }

            log.info(Arrays.asList(payload));

            // Implementa tu lógica aquí
            log.info("Received notification: " + payload);

            String paymentId = (String) ((Map<String, Object>) payload.get("data")).get("id");

            Payment payment = mercadoPagoClient.getPayment(paymentId, mercadolibreToken);

            MercadoPagoConfig.setAccessToken(mercadolibreToken);
            List<PaymentItem> items = payment.getAdditionalInfo().getItems();
            for (PaymentItem item : items) {
                String productName = item.getTitle();
                int quantity = item.getQuantity();
                BigDecimal price = item.getUnitPrice();
                log.info("productName: " + productName);
                log.info("quantity: " + quantity);
                log.info("price: " + price);

                // Procesa la información según sea necesario
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok("Received");
    }
}
