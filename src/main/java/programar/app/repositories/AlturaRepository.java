package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Altura;

@Repository
public interface AlturaRepository extends JpaRepository<Altura, Long> {

}
