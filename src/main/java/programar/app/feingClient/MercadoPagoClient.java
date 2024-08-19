package programar.app.feingClient;

import com.mercadopago.resources.merchantorder.MerchantOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mercadoPagoClient", url = "https://api.mercadopago.com")
public interface MercadoPagoClient {

    @GetMapping("/merchant_orders/{id}")
    MerchantOrder getMerchantOrder(@RequestParam("id") String id,
                                   @RequestParam("Authorization") String accessToken);

    @GetMapping("/merchant_orders/search")
    MerchantOrder getOrder(@RequestParam("status") String status,
       @RequestParam("preference_id") String preference_id,
       @RequestParam("pplication_id") String pplication_id,
       @RequestParam("payer_id") String payer_id,
       @RequestParam("sponsor_id") String sponsor_id,
       @RequestParam("external_reference") String external_reference,
       @RequestParam("site_id") String site_id,
       @RequestParam("marketplace") String marketplace,
       @RequestParam("date_created_from") String date_created_from,
       @RequestParam("date_created_to") String date_created_to,
       @RequestParam("last_updated_from") String last_updated_from,
       @RequestParam("last_updated_to") String last_updated_to,
       @RequestParam("offset") String offset,
       @RequestParam("items") String items,
       @RequestParam(name = "Authorization", required = true) String accessToken);



    @GetMapping("/merchant_orders/search")
    MerchantOrder getOrderByPreferenceId(@RequestParam(name = "status", value = "", required = false) String status,
         @RequestParam(name = "preference_id", required = true) String preference_id,
         @RequestParam(name ="pplication_id", value = "", required = false) String pplication_id,
         @RequestParam(name ="payer_id", value = "", required = false) String payer_id,
         @RequestParam(name ="sponsor_id", value = "", required = false) String sponsor_id,
         @RequestParam(name ="external_reference", value = "", required = false) String external_reference,
         @RequestParam(name ="site_id", value = "", required = false) String site_id,
         @RequestParam(name ="marketplace", value = "", required = false) String marketplace,
         @RequestParam(name ="date_created_from", value = "", required = false) String date_created_from,
         @RequestParam(name ="date_created_to", value = "", required = false) String date_created_to,
         @RequestParam(name ="last_updated_from", value = "", required = false) String last_updated_from,
         @RequestParam(name ="last_updated_to", value = "", required = false) String last_updated_to,
         @RequestParam(name ="offset", value = "", required = false) String offset,
         @RequestParam(name ="items", value = "", required = false) String items,
         @RequestParam(name = "Authorization", required = true) String accessToken);

}