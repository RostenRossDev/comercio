package programar.app.services.impl;

import com.mercadopago.resources.merchantorder.MerchantOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import programar.app.entities.Venta;
import programar.app.feingClient.MercadoPagoClient;
import programar.app.repositories.VentaRepository;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class PaymentCheckServiceImpl {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private MercadoPagoClient mercadoPagoClient;

    @Value("${codigo.mercadoLibre}")
    private String mercadolibreToken;

    @Scheduled(fixedRate = 300000) // Ejecutar cada 60 segundos (ajustar según sea necesario)
    public void checkPayments() {
        List<Venta> ventas = ventaRepository.findByPagadoFalseAndIsValidoTrue();

        List<MerchantOrder> orders =  new ArrayList<>();

        if(ventas.size() > 0){
            ventas.forEach(item -> {
                MerchantOrder merchantOrder = mercadoPagoClient.getMerchantOrder(item.getPreferenceId(), mercadolibreToken);
                orders.add(merchantOrder);
            });

            orders.forEach(log::info);
        }
    }
}
