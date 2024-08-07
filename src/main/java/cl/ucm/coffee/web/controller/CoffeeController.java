package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.service.ICoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/coffee")
public class CoffeeController {
    @Autowired
    private ICoffeeService coffeeService;

    @GetMapping("/search") //Busca café
    public ResponseEntity<?> findByName(@RequestParam(name = "name") String name){
        try {
            List<CoffeeEntity> coffees = coffeeService.findByName(name);
            return ResponseEntity.ok(coffees);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al encontrar café por nombre: " + e.getMessage());
        }
    }

    @GetMapping("/list") //Lista de cafés en la base de datos
    public ResponseEntity<List<CoffeeEntity>> coffeeList() {
        try {
            List<CoffeeEntity> coffees = coffeeService.coffeeList();
            return ResponseEntity.ok(coffees);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    @PostMapping("/coffees") //Crear café
    public ResponseEntity<?> saveCoffees(@RequestParam(name = "name") String name,
                                         @RequestParam(name = "price") Integer price,
                                         @RequestParam(name = "desc") String description,
                                         @RequestParam(name = "image64") MultipartFile image64){
        try {
            CoffeeEntity coffeeEntity = new CoffeeEntity();
            coffeeEntity.setName(name);
            coffeeEntity.setPrice(price);
            coffeeEntity.setDescription(description);
            coffeeEntity.setImage64(image64.getBytes());

            CoffeeEntity savedCoffee = coffeeService.saveCoffee(coffeeEntity);
            return ResponseEntity.ok(savedCoffee);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar café: " + e.getMessage());
        }
    }

    @PutMapping("/updateCoffee") //Actualizar café
    public ResponseEntity<?> updateCoffee(@RequestParam(name = "id_coffee") Integer idCoffee,
                                          @RequestParam(name = "name") String name,
                                          @RequestParam(name = "price") Integer price,
                                          @RequestParam(name = "desc") String description,
                                          @RequestParam(name = "image64") MultipartFile image64) {
        try {
            CoffeeEntity updatedCoffee = new CoffeeEntity();
            updatedCoffee.setIdCoffee(idCoffee);
            updatedCoffee.setName(name);
            updatedCoffee.setPrice(price);
            updatedCoffee.setDescription(description);
            updatedCoffee.setImage64(image64.getBytes());

            Optional<CoffeeEntity> results = coffeeService.updateCoffee(idCoffee, updatedCoffee);
            return ResponseEntity.ok(results.isPresent() ? results.get() : "No se encontró el café");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el café: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteCoffee") //Eliminar café
    public ResponseEntity<?> deleteCoffee(@RequestParam(name = "id_coffee") int idCoffee) {
        try {
            boolean deleted = coffeeService.deleteCoffeeById(idCoffee);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(404).body("Café no encontrado");
            }
        } catch (Exception e){
            return ResponseEntity.status(500).body("Error al eliminar el café: " + e.getMessage());
        }
    }
}
