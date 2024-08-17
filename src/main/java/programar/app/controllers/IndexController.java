package programar.app.controllers;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import programar.app.entities.Product;
import programar.app.entities.Transaccion;
import programar.app.entities.Venta;
import programar.app.repositories.TransaccionRepository;
import programar.app.repositories.VentaRepository;
import programar.app.services.ProductService;
import programar.app.utils.ListSplitter;

import java.util.List;

@Log4j2
@Controller
public class IndexController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;


    @Autowired
    private ProductService productService;


    @GetMapping("/inicio")
    public String index(Model model){

        List<Product> ofertas = productService.ofertas();
        List<List<Product>> ofertasPantallaGrande = ListSplitter.splitListIntoSublistsOfFour(ofertas);
        List<List<Product>> ofertasPantallaChica = ListSplitter.splitListIntoSublistsOfTwo(ofertas);

        List<Product> products = productService.productosHabilitados();
        List<List<Product>> productosPantallaGrande = ListSplitter.splitListIntoSublistsOfFour(products);
        List<List<Product>>  productosPantallaChica = ListSplitter.splitListIntoSublistsOfTwo(products);

        model.addAttribute("ofertasPantallaChica", ofertasPantallaChica);
        model.addAttribute("ofertasPantallaGrande", ofertasPantallaGrande);

        model.addAttribute("productosPantallaGrande", productosPantallaGrande);
        model.addAttribute("productosPantallaChica", productosPantallaChica);

        return "index";
    }

    @GetMapping("/pay")
    public String pay(Model model){
        return "pay";
    }



    @GetMapping({"/", ""})
    public String goHome(){
        return "redirect:/inicio";
    }

    @PostMapping({"/", ""})
    public String redirectHome(){
        return "redirect:/inicio";
    }

    @GetMapping("/process_transaction")
    public String processTransaction(@RequestParam(name = "collection_id") Long collection_id,
                                     @RequestParam(name ="collection_status") String collection_status,
                                     @RequestParam(name = "payment_id") Long payment_id,
                                     @RequestParam(name = "status") String status,
                                     @RequestParam(name = "external_reference") String external_reference,
                                     @RequestParam(name = "payment_type") String payment_type,
                                     @RequestParam(name = "merchant_order_id") Long merchant_order_id,
                                     @RequestParam(name = "preference_id") String preference_id,
                                     @RequestParam(name = "site_id") String site_id,
                                     @RequestParam(name = "processing_mode") String processing_mode,
                                     @RequestParam("merchant_account_id") String merchant_account_id) {

        log.info("CollectionId: {}, CollectionStatus: {}, payment_id: {}" +
                        ", Status: {}, ExternalReference: {}, paymentType: {}" +
                        ", MerchantOrderId: {}, PreferenceId: {}, siteId: {}" +
                        ", ProcessingMode: {}, MerchantAccountId: {}", collection_id, collection_status, payment_id, status,
                external_reference, payment_type, merchant_order_id, preference_id, site_id, processing_mode, merchant_account_id);


        Venta venta = ventaRepository.findByPreferenceId(preference_id); // Suponiendo que guardaste el preference_id en la Venta

        Transaccion transaccion = new Transaccion();
        transaccion.setCollectionId(collection_id);
        transaccion.setCollectionStatus(collection_status);
        transaccion.setPaymentId(payment_id);
        transaccion.setStatus(status);
        transaccion.setExternalReference(external_reference);
        transaccion.setPaymentType(payment_type);
        transaccion.setMerchantOrderId(merchant_order_id);
        transaccion.setPreferenceId(preference_id);
        transaccion.setSiteId(site_id);
        transaccion.setProcessingMode(processing_mode);
        transaccion.setMerchantAccountId(merchant_account_id);
        transaccion.setVenta(venta);

        transaccionRepository.save(transaccion);
        return "redirect:/inicio";
    }
}
