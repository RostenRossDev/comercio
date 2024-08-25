package programar.app.controllers;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import programar.app.dtos.EntregaRequest;
import programar.app.entities.Factura;
import programar.app.entities.Item;
import programar.app.entities.Venta;
import programar.app.repositories.ItemRepository;
import programar.app.repositories.TransaccionRepository;
import programar.app.repositories.VentaRepository;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class FcturacionController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @GetMapping("/facturacion")
    public String facturas(@RequestParam(name = "email", required = false) String emal,
                           @RequestParam(name = "retirado", required = false) Boolean retirado,
                           @RequestParam(name = "pagado", required = false) Boolean pagado,
                           Model model){

        List<Venta> ventas = ventaRepository.findByEntregadoFalseAndIsValidoTrue();
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

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value="id") Long id,
                      Model model, RedirectAttributes flash){

        Venta venta= ventaRepository.findById(id).orElse(null); //clienteService.findFacturaById(id);
        //Se reemplaza el metodo findFacturaById por fetchByIdWithClienteWithItemFacturaWithProducto, debido a que la primera hace 7 consultas y la ultima solo 1
        if (venta == null) {
            flash.addFlashAttribute("error","La factura no existe en la base de datos");
            return "redirect:/lsitar";
        }
        model.addAttribute("factura", venta.getFactura());
//        model.addAttribute("titulo", "Factura: ".concat(venta.getDescripcion()));

        return "factura/ver";
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
