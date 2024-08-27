package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Altura;
import programar.app.entities.Venta;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Venta findByPreferenceId(String preferenceId);
    List<Venta>  findByPagadoFalseAndIsValidoTrue();
    List<Venta>  findByEntregadoFalseAndIsValidoTrue();
    List<Venta>  findByEntregadoFalseAndIsValidoTrueAndFacturaIsNotNull();
}
