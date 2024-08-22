package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Item;
import programar.app.entities.Venta;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
