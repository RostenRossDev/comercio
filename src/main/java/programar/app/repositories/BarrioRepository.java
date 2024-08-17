package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Barrio;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {

}
