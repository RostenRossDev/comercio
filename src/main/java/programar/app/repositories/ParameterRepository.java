package programar.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.Altura;
import programar.app.entities.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    Parameter findByName(String name);
    Parameter findByActualValue(String actualValue);
}
