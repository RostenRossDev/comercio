package programar.app.controllers;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import programar.app.entities.*;
import programar.app.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@RestController
public class MpController {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private AlturaRepository alturaRepository;

    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private CalleRepository calleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Value("${codigo.mercadoLibre}")
    private String mercadolibreToken;


    @PostMapping("fill-venta")
    public ResponseEntity<?> fillVenta(@RequestBody BuyerData userBuyer){

        Cliente cliente = clienteRepository.findByEmail(userBuyer.getEmail());
        log.info("cliente existe ?: " + cliente);

        cliente = fillCliente(userBuyer, cliente);
        log.info("cliente: " + cliente);

        List<Venta> ventas = fillVenta(userBuyer, cliente);
        log.info("ventas: " + ventas);
        return ResponseEntity.ok("");
    }

    @RequestMapping(value="create_preference", method = RequestMethod.POST)
    public String createPreference(@RequestBody UserBuyer userBuyer){
        log.info(userBuyer);
        log.info("aca  1");


        if(userBuyer == null){
            log.info("error 1");
            return "error json:/";
        }

        /////////////////////////////////////////////////////////////////////////
        log.info("pas 1");
        log.info(userBuyer);
        try {
            MercadoPagoConfig.setAccessToken(mercadolibreToken);
            //-------------------------------------------------------------------Creacion de preferencias
            List<PreferenceItemRequest> items = new ArrayList<>();


            // 1 - preferencias de venta


            userBuyer.getItems().forEach(item -> {
                log.info("userBuyer: " + userBuyer);
                log.info("unitPrice: " + userBuyer.getItems().getFirst().getPrice());
                log.info("descuento: " + userBuyer.getItems().getFirst().getDiscount());
                log.info("total: " + userBuyer.getItems().getFirst().getPrice());

                String title = item.getName();
                Integer quantity = item.getQuantity();
                Integer discount = item.getDiscount();
                Double price =Double.parseDouble(item.getPrice());
                Double totalBeforeDiscount = price * quantity;
                Double discountAmount = totalBeforeDiscount * (discount / 100.0);
                Double total = totalBeforeDiscount - discountAmount;

                String description = String.format("%s : %s con %d de descuento aplicado.", title, price.toString(), item.getDiscount());
                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                        .id(item.getId().toString())
                        .title(title)
                        .quantity(quantity)
                        .unitPrice(new BigDecimal(total))
                        .description(title + " x"+quantity)
                        .currencyId("ARS")
                        .build();
                log.info("unitPrice: " + price);
                log.info("itemRequest unitPrice: " + itemRequest.getUnitPrice());

                items.add(itemRequest);
            });

            items.forEach(log::info);
            log.info("paso 2");

            //2 - Preferencia de control de sucesos
            PreferenceBackUrlsRequest backUrls= PreferenceBackUrlsRequest
                    .builder()
                    .success("https://localhost:8080/process_transaction")
                    .pending("https://localhost:8080/inicio")
                    .failure("https://localhost:8080/inicio")
                    .build();


            //----------------------------------------------------------------------- ENSAMBLE DE PREFERENCIAS
            log.info("paso 3");
            String externalReference = generateExternalReference();
            //Creo una preferencia que contenga todas las preferencias que haya creado
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("aproved")
                    .notificationUrl("https://0d4b-2803-9800-94c0-7bfc-1c6f-30b-f8b9-4e4d.ngrok-free.app/webhooks/notification")
                    .externalReference(externalReference)
                    .build();
            log.info("paso 4");

            //Creo un objeto tipo cliente para comunicarce con MP
            PreferenceClient client = new PreferenceClient();
            //Creo una nueva preferencia que sera igual a la respuesta que nuestro cliente nos crearar a partir de la informacion quye le enviamos
            log.info("paso 5");
            log.info("preferenceRequest: " + preferenceRequest.toString());

            Preference preference = client.create(preferenceRequest);
            log.info("preference: " + preference.toString());
            log.info("preference: " + preference.getId());

            log.info("paso 6");

            Venta newVenta = new Venta();
            newVenta.setPreferenceId(preference.getId());
            log.info("paso 7");

            ventaRepository.save(newVenta);

            //----------------------------------------------------------------------- retornamos preferencia
            //retornamos esa prefrencia al frontend
            log.info("preference: " + preference.getId());
            return preference.getId();
        } catch (MPApiException e){
            var apiResponse = e.getApiResponse();
            var content = apiResponse.getContent();
            log.info("error: " + content);
            e.printStackTrace();
            return e.toString();
        }catch (MPException e){
            e.printStackTrace();
            return e.toString();
        }

    }

    private Cliente fillCliente(BuyerData userBuyer, Cliente cliente){

        Calle calle;
        Barrio barrio;
        Altura altura;
        Direccion direccion;

        //Barrio
        if( cliente != null && cliente.getDirecciones().get(0).getBarrio() != null){
            barrio = cliente.getDirecciones().get(0).getBarrio();
            if(userBuyer.getBarrio() != null && !userBuyer.getBarrio().equals(barrio.getNombre())){
                barrio.setNombre(userBuyer.getBarrio());
            }
        }else {
            barrio = new Barrio();
            barrio.setNombre(userBuyer.getBarrio());
            barrio = barrioRepository.save(barrio);
        }

        //Calle
        if(cliente != null && cliente.getDirecciones().get(0).getCalle() != null){
            calle = cliente.getDirecciones().get(0).getCalle();
            if(!userBuyer.getCalle().equals(calle.getNombre())){
                calle.setNombre(userBuyer.getCalle());
            }
        }else {
            calle = new Calle();
            calle.setNombre(userBuyer.getCalle());
            calle = calleRepository.save(calle);
        }

        //Alutra
        if(cliente != null &&  cliente.getDirecciones().get(0).getAltura() != null){
            altura  = cliente.getDirecciones().get(0).getAltura();
            if(!userBuyer.getAltura().equals(altura.getNumero())){
                altura.setNumero(userBuyer.getAltura());
            }
        }else {
            altura = new Altura();
            altura.setNumero(userBuyer.getAltura());
            altura = alturaRepository.save(altura);
        }

        //Direccion
        if(cliente != null && cliente.getDirecciones().get(0) != null){
            direccion = cliente.getDirecciones().get(0);
            if(!userBuyer.getCalle().equals(direccion.getCalle().getNombre())){
                direccion.getCalle().setNombre(userBuyer.getCalle());
            }

            if(userBuyer.getAltura() != null && !userBuyer.getAltura().equals(direccion.getAltura().getNumero())){
                direccion.getAltura().setNumero(userBuyer.getAltura());
            }

            if(userBuyer.getBarrio() != null && !userBuyer.getBarrio().equals(direccion.getBarrio().getNombre())){
                direccion.getBarrio().setNombre(userBuyer.getBarrio());
            }
        }else {
            direccion = new Direccion();
            direccion.setAltura(altura);
            direccion.setCalle(calle);
            direccion.setBarrio(barrio);
            direccion = direccionRepository.save(direccion);
        }

        if(cliente == null){
            cliente = new Cliente();
            List<Direccion> direccionList = new ArrayList<>();
            cliente.setDirecciones(direccionList);
        }

        //cliente
        cliente.getDirecciones().add(direccion);
        cliente.setTelefono(userBuyer.getTelefono());
        cliente.setEmail(userBuyer.getEmail());

        return clienteRepository.save(cliente);
    }

    private List<Venta> fillVenta(BuyerData userBuyer, Cliente cliente){
        List<Venta> ventas = new ArrayList<>();
        userBuyer.getItems().forEach(item ->{
            Venta venta = new Venta();
            venta.setProducto(item.getName());
            venta.setCantidad(item.getQuantity());
            venta.setPrecio(new BigDecimal(item.getPrice()));
            venta.setCliente(cliente);
            venta.setPreferenceId(userBuyer.getPreferenceId()); // Asigna el preferenceId
            ventas.add(ventaRepository.save(venta));
        });
        return ventas;
    }

    private String generateExternalReference(){
        Random random = new Random();
        int randomNumber = random.nextInt(10000); // Puedes ajustar el rango según sea necesario

        // Define la referencia externa, incluye un número aleatorio
        return "external_ref_" + randomNumber + "_" + System.currentTimeMillis();
    }
}
