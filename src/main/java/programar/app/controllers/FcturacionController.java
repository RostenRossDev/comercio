package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import programar.app.entities.Venta;
import programar.app.repositories.TransaccionRepository;
import programar.app.repositories.VentaRepository;

import java.util.List;

@Controller
@Log4j2
public class FcturacionController {

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
}
