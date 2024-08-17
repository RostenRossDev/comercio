package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Altura;
import programar.app.entities.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Venta findByPreferenceId(String preferenceId);
}
