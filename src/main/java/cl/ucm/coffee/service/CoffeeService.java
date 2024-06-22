package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CoffeeService implements ICoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Override
    public List<CoffeeEntity> coffeeList() {
        return coffeeRepository.findAll();
    }

    @Override
    @Transactional
    public CoffeeEntity saveCoffee(CoffeeEntity coffeeEntity) {
        try {
            return coffeeRepository.save(coffeeEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar café: " + e.getMessage());
        }
    }

    @Override
    public List<CoffeeEntity> findByName(String name) {
        try {
            return coffeeRepository.findByName(name);
        } catch (Exception e) {
            throw new RuntimeException("Error al encontrar café por nombre: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteCoffeeById(Integer idCoffee) {
        Optional<CoffeeEntity> coffeeEntity = coffeeRepository.findById(idCoffee);
        if (coffeeEntity.isPresent()) {
            try {
                coffeeRepository.deleteById(idCoffee);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar el café: " + e.getMessage());
            }
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Optional<CoffeeEntity> updateCoffee(Integer idCoffee, CoffeeEntity updatedCoffee) {
        Optional<CoffeeEntity> existCoffee = coffeeRepository.findById(idCoffee);
        if (existCoffee.isPresent()) {
            try {
                CoffeeEntity coffeeEntity = existCoffee.get();
                coffeeEntity.setName(updatedCoffee.getName());
                coffeeEntity.setPrice(updatedCoffee.getPrice());
                coffeeEntity.setDescription(updatedCoffee.getDescription());
                coffeeEntity.setImage64(updatedCoffee.getImage64());
                return Optional.of(coffeeRepository.save(coffeeEntity));
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar el café: " + e.getMessage());
            }
        } else {
            return Optional.empty();
        }
    }
}
