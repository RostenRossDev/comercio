package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Barrio;
import programar.app.entities.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

}
