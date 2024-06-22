package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;

import java.util.List;
import java.util.Optional;

public interface ICoffeeService {
    List<CoffeeEntity> coffeeList();
    CoffeeEntity saveCoffee(CoffeeEntity coffeeEntity);
    List<CoffeeEntity> findByName(String name);
    boolean deleteCoffeeById(Integer coffeeId);
    Optional<CoffeeEntity> updateCoffee(Integer idCoffee, CoffeeEntity updatedCoffee);
}
