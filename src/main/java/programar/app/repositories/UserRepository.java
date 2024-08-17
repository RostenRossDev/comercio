package programar.app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import programar.app.entities.CustomUser;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {

    CustomUser findByUsername(String username);
}