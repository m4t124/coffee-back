package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coffee")
public class CoffeeController {

    @Autowired
    private CoffeeRepository coffeeRepository;

    //Crear nuevo café
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "price") int price,
            @RequestParam(name = "desc") String description,
            @RequestParam(name = "foto") MultipartFile foto) {
        try {
            CoffeeEntity newCoffee = new CoffeeEntity();
            newCoffee.setName(name);
            newCoffee.setPrice(price);
            newCoffee.setDescription(description);
            newCoffee.setImage64(Base64.getEncoder(), encodeToString(foto.getBytes()));

            coffeeRepository.save(newCoffee);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Café creado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al crear el café");
            return ResponseEntity.status(500).body(response);
        }

    }

    //Buscar café por nombre
    @GetMapping("/findByName")
    public ResponseEntity<?> findByName(@RequestParam(name = "name") String name) {
        Optional<CoffeeEntity> coffee = coffeeRepository.findByName(name);
        if (coffee.isPresent()) {
            return ResponseEntity.ok(coffee.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Café no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    //Listar todos los cafés
    @GetMapping("/list")
    public ResponseEntity<?> coffeeList() {
        return ResponseEntity.ok(coffeeRepository.findAll());
    }

    //Actualizar un café
    @PutMapping("/updateCoffee")
    public ResponseEntity<?> updateCoffee(@RequestBody CoffeeEntity coffeeEntity) {
        if (coffeeRepository.existsById(coffeeEntity.getIdCoffee())) {
            coffeeRepository.save(coffeeEntity);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Café actualizado");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Café no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    //Eliminar un café por ID
    @DeleteMapping("/deleteCoffee")
    public ResponseEntity<?> deleteCoffee(@RequestParam(name = "idCoffee") int idCoffee) {
        if (coffeeRepository.existsById(idCoffee)) {
            coffeeRepository.deleteById(idCoffee);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Café eliminado");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Café no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }
}

