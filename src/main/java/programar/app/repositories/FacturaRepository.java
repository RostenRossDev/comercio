package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Factura;
import programar.app.entities.Venta;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Factura findByVentaId(Long ventaId);
}
