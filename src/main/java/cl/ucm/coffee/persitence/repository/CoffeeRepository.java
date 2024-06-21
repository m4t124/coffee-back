package cl.ucm.coffee.persitence.repository;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoffeeRepository extends JpaRepository<CoffeeEntity, Integer> {
    List<CoffeeEntity> findByName(String name);

}
