package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CoffeeEntity saveCoffee(CoffeeEntity coffeeEntity) {
        return coffeeRepository.save(coffeeEntity);
    }

    @Override
    public List<CoffeeEntity> findByName(String name) {
        return coffeeRepository.findByName(name);
    }

    @Override
    public boolean deleteCoffeeById(Integer coffeeId) {
        Optional<CoffeeEntity> coffeeEntity = coffeeRepository.findById(coffeeId);
        if(coffeeEntity.isPresent()){
            coffeeRepository.deleteById(coffeeId);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Optional<CoffeeEntity> updateCoffee(Integer coffeeId, CoffeeEntity updateCoffee) {
        Optional<CoffeeEntity> existCoffee = coffeeRepository.findById(coffeeId);
        if (existCoffee.isPresent()) {
            CoffeeEntity coffeeEntity = existCoffee.get();
            coffeeEntity.setName(updateCoffee.getName());
            coffeeEntity.setPrice(updateCoffee.getPrice());
            coffeeEntity.setDescription(updateCoffee.getDescription());
            coffeeEntity.setImage64(updateCoffee.getImage64());
            return Optional.of(coffeeRepository.save(coffeeEntity));
        }else {
            return Optional.empty();
        }
    }
}
