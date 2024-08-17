package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Calle;

@Repository
public interface CalleRepository extends JpaRepository<Calle, Long> {

}
