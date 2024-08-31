package programar.app.controllers;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import programar.app.dtos.EntregaRequest;
import programar.app.entities.Factura;
import programar.app.entities.Item;
import programar.app.entities.Parameter;
import programar.app.entities.Venta;
import programar.app.repositories.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class FcturacionController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ParameterRepository parameterRepository;

    @GetMapping("/facturacion")
    public String facturas(@RequestParam(name = "email", required = false) String emal,
                           @RequestParam(name = "retirado", required = false) Boolean retirado,
                           @RequestParam(name = "pagado", required = false) Boolean pagado,
                           Model model){

        Parameter paramProductButton = parameterRepository.findByName("productButton");
        Parameter paramHeroTitle = parameterRepository.findByName("heroTitle");
        Parameter paramHeroText = parameterRepository.findByName("heroText");
        Parameter paramOfertSection = parameterRepository.findByName("ofertSection");
        Parameter paramProductSection = parameterRepository.findByName("productSection");
        Parameter paramPhone = parameterRepository.findByName("phone");
        Parameter paramEmail = parameterRepository.findByName("email");
        Parameter paramYoutube = parameterRepository.findByName("youtube");
        Parameter paramTwitter = parameterRepository.findByName("twitter");
        Parameter paramInstagram = parameterRepository.findByName("instagram");
        Parameter paramFacebook = parameterRepository.findByName("facebook");
        Parameter paramSiteName = parameterRepository.findByName("siteName");

        model.addAttribute("productButton", paramProductButton);
        model.addAttribute("siteName", paramSiteName);
        model.addAttribute("facebook", paramFacebook);
        model.addAttribute("instagram", paramInstagram);
        model.addAttribute("twitter", paramTwitter);
        model.addAttribute("youtube", paramYoutube);
        model.addAttribute("email", paramEmail);
        model.addAttribute("phone", paramPhone);
        model.addAttribute("productSection", paramProductSection);
        model.addAttribute("ofertSection", paramOfertSection);
        model.addAttribute("heroText", paramHeroText);
        model.addAttribute("heroTitle", paramHeroTitle);

        List<Venta> ventas = ventaRepository.findByEntregadoFalseAndIsValidoTrueAndFacturaIsNotNull();
        ventas = ventas.stream().map(item -> {

            Factura factura = facturaRepository.findByVentaId(item.getId());
            log.info("fatura: " + factura);
            item.setFactura(factura);
            return item;
        }).collect(Collectors.toUnmodifiableList());
        log.info(ventas);
        model.addAttribute("ventas", ventas);
        return "invoice";
    }

    @GetMapping("/entregar")
    public String entregar(@RequestParam(name="itemId")  Long itemId,  @RequestParam(name="ventaId")  Long ventaId, Model model){

        Item item = itemRepository.findById(itemId).orElse(null);
        Venta venta = ventaRepository.findById(ventaId).orElse(null);
        if(item != null && venta != null){
            item.setEntregado(Boolean.TRUE);
            itemRepository.save(item);
            Boolean isEntregado = verifyEntregado(venta.getItems());
            venta.setEntregado(isEntregado);
            ventaRepository.save(venta);
        }
        return "redirect:/facturacion";
    }

    @GetMapping("/entregarTodo")
    public String entregarTodo(@RequestParam(name="ventaId") Long ventaId, Model model){

        Venta venta = ventaRepository.findById(ventaId).orElse(null);
        if(venta != null){
            List<Item> items = venta.getItems().stream().map(it -> {
                it.setEntregado(Boolean.TRUE);
                itemRepository.save(it);
                return it;
            }).collect(Collectors.toList());
            venta.setItems(items);
            venta.setEntregado(Boolean.TRUE);
            ventaRepository.save(venta);
        }
        return "redirect:/facturacion";
    }

    @GetMapping("/facturacion/exportar/{id}")
    public String  exportarFacturaPDF(@PathVariable("id") Long id, Model  model) {
        log.info("exportarFacturaPDF");
        log.info("id: " + id);
        // Obtener la factura por ID
        Factura factura  = facturaRepository.findById(id).orElse(null);
        log.info("factura: " + factura);

        Venta venta = factura.getVenta();
        log.info("fact: " + factura);
        // Asegúrate de manejar el caso donde la factura no se encuentra
        if (factura == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada");
        }

        // Preparar el modelo y la vista para generar el PDF;
        model.addAttribute("factura", factura);

        return "invoicePdfView";
    }


    private Boolean verifyEntregado(List<Item> items){
        boolean allItemsEntregados = true;

        for (Item it: items) {

            if (!it.getEntregado()) {
                allItemsEntregados = false;
                break; // No necesitamos seguir buscando si encontramos uno en false
            }

            if (!allItemsEntregados) {
                break; // Salimos del bucle si ya encontramos un false en cualquier elemento
            }
        }

        return allItemsEntregados;
    }
}
