package programar.app.feingClient;

import com.mercadopago.resources.merchantorder.MerchantOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class  MercadoPagoClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.mercadopago.com/merchant_orders/search";

    public MerchantOrder getMerchantOrder(String id, String accessToken) {

        // Construir la URL
        String url = BASE_URL + "/merchant_orders/" + id;

        // Crear los headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Crear la entidad con los headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET
        ResponseEntity<MerchantOrder> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MerchantOrder.class
        );

        return response.getBody();
    }

    public MerchantOrder getOrder(String status, String preferenceId, String applicationId, String payerId,
                                  String sponsorId, String externalReference, String siteId, String marketplace,
                                  String dateCreatedFrom, String dateCreatedTo, String lastUpdatedFrom,
                                  String lastUpdatedTo, String offset, String items, String accessToken) {

        // Construir la URL
        String url = BASE_URL + "/merchant_orders/search"
                + "?status=" + status
                + "&preference_id=" + preferenceId
                + "&application_id=" + applicationId
                + "&payer_id=" + payerId
                + "&sponsor_id=" + sponsorId
                + "&external_reference=" + externalReference
                + "&site_id=" + siteId
                + "&marketplace=" + marketplace
                + "&date_created_from=" + dateCreatedFrom
                + "&date_created_to=" + dateCreatedTo
                + "&last_updated_from=" + lastUpdatedFrom
                + "&last_updated_to=" + lastUpdatedTo
                + "&offset=" + offset
                + "&items=" + items;

        // Crear los headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Crear la entidad con los headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realizar la solicitud GET
        ResponseEntity<MerchantOrder> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MerchantOrder.class
        );

        return response.getBody();
    }

}