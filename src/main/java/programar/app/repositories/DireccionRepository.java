package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

}
