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
import programar.app.dtos.CartItem;
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

    @Autowired
    private ItemRepository itemRepository;

    @Value("${codigo.mercadoLibre}")
    private String mercadolibreToken;

    public void createVenta(String barrio, String calle, Integer altura, String email,
                            String telefono, String preferenceId, String nombre, String apellido, List<CartItem> items){

        Cliente cliente = clienteRepository.findByEmail(email);
        log.info("cliente existe ?: " + cliente);

        cliente = fillCliente(cliente, barrio, calle, altura, email, telefono, nombre, apellido, true);
        log.info("cliente: " + cliente);

        Venta venta = fillVenta(cliente,preferenceId);

        log.info("ventas: " + venta);

        List<Item> itemList = new ArrayList<>();
        items.forEach(it -> {
            Item item = new Item();
            item.setProductId(it.getId());
            item.setEntregado(false);
            item.setCurrencyId("ARS");
            item.setTitle(it.getName());
            item.setUnitPrice(Double.parseDouble(it.getPrice()));
            item.setQuantity(it.getQuantity());
            item.setDiscount(it.getDiscount());
            itemList.add(item);
        });
        itemRepository.saveAll(itemList);
        venta.setItems(itemList);
        log.info("paso 7");
        venta.setGranTotal();
        ventaRepository.save(venta);
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


        String nombre = userBuyer.getNombre();
        String apellido = userBuyer.getApellido();
        String email =  userBuyer.getEmail();
        String calle =  userBuyer.getCalle();
        Integer altura = userBuyer.getAltura();
        String barrio = userBuyer.getBarrio();
        String casa = userBuyer.getCasa();
        String departamento= userBuyer.getDepartamento();
        String piso = userBuyer.getCasa();
        String entreCalles = userBuyer.getEntreCalles();
        String telefono = userBuyer.getTelefono();
        String descripcion = userBuyer.getDescripcion();
        String rangoEntrega = userBuyer.getRangoEntrega();

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
                    .autoReturn("approved")
                    .notificationUrl("https://6900-2803-9800-94c0-7bfc-d83e-745b-f7b0-e90d.ngrok-free.app/webhooks/notification")
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

            createVenta(barrio, calle, altura, email, telefono, preference.getId(), nombre, apellido, userBuyer.getItems());

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

    private Cliente fillCliente(Cliente cliente, String barrioStr, String calleStr, Integer alturaInt, String email, String telefono, String nombre, String apellido, Boolean retiroEnLocal){

        Calle calle = null;
        Barrio barrio = null;
        Altura altura = null;
        Direccion direccion = null;

        if(!retiroEnLocal) {
            //Barrio
            if (cliente != null && cliente.getDirecciones().get(0).getBarrio() != null) {
                barrio = cliente.getDirecciones().get(0).getBarrio();
                if (barrioStr != null && !barrioStr.equals(barrio.getNombre())) {
                    barrio.setNombre(barrioStr);
                }
            } else {
                barrio = new Barrio();
                barrio.setNombre(barrioStr);
                barrio = barrioRepository.save(barrio);
            }

            //Calle
            if (cliente != null && cliente.getDirecciones().get(0).getCalle() != null) {
                calle = cliente.getDirecciones().get(0).getCalle();
                if (calleStr.equals(calle.getNombre())) {
                    calle.setNombre(calleStr);
                }
            } else {
                calle = new Calle();
                calle.setNombre(calleStr);
                calle = calleRepository.save(calle);
            }

            //Alutra
            if (cliente != null && cliente.getDirecciones().get(0).getAltura() != null) {
                altura = cliente.getDirecciones().get(0).getAltura();
                if (!alturaInt.equals(altura.getNumero())) {
                    altura.setNumero(alturaInt);
                }
            } else {
                altura = new Altura();
                altura.setNumero(alturaInt);
                altura = alturaRepository.save(altura);
            }

            //Direccion
            if (cliente != null && cliente.getDirecciones().get(0) != null) {
                direccion = cliente.getDirecciones().get(0);
                if (!calleStr.equals(direccion.getCalle().getNombre())) {
                    direccion.setCalle(calle);
                }

                if (alturaInt != null && !alturaInt.equals(direccion.getAltura().getNumero())) {
                    direccion.setAltura(altura);
                }

                if (barrioStr != null && barrioStr.equals(direccion.getBarrio().getNombre())) {
                    direccion.setBarrio(barrio);
                }
            } else {
                direccion = new Direccion();
                direccion.setAltura(altura);
                direccion.setCalle(calle);
                direccion.setBarrio(barrio);
                direccion = direccionRepository.save(direccion);
            }
        }
        if (cliente == null) {
            cliente = new Cliente();
            List<Direccion> direccionList = new ArrayList<>();
            cliente.setDirecciones(direccionList);
        }

        if(!retiroEnLocal) {
            cliente.getDirecciones().add(direccion);
        }
        //cliente
        cliente.setTelefono(telefono);
//        cliente.setEmail(email);
        cliente.setEmail("rosten2016@gmail.com");
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        return clienteRepository.save(cliente);
    }

    private Venta fillVenta(Cliente cliente, String preferenceId){
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setPreferenceId(preferenceId); // Asigna el preferenceId
        venta.setPagado(false);
        log.info("venta: " + venta);
        Factura factura = new Factura();
        factura.setVenta(venta);
        venta.setFactura(factura);
        venta = ventaRepository.save(venta);
        return venta;
    }

    private String generateExternalReference(){
        Random random = new Random();
        int randomNumber = random.nextInt(10000); // Puedes ajustar el rango según sea necesario

        // Define la referencia externa, incluye un número aleatorio
        return "external_ref_" + randomNumber + "_" + System.currentTimeMillis();
    }
}
