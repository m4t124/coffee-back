package cl.ucm.coffee.persitence.repository;

import cl.ucm.coffee.persitence.entity.TestimonialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonialsRepository extends JpaRepository<TestimonialsEntity, Integer> {
    List<TestimonialsEntity> findByIdCoffee(int idCoffee);
}
