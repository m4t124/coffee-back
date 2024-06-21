package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.service.CoffeeService;
import cl.ucm.coffee.service.ICoffeeService;
import com.fasterxml.jackson.databind.ser.Serializers;
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

    //Buscar café
    @GetMapping("/search")
    public ResponseEntity<?> findByName(@RequestParam(name = "name") String name){
        try {
            List<CoffeeEntity> coffees = coffeeService.findByName(name);
            return ResponseEntity.ok(coffees);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    //Listar todos los cafés
    @GetMapping("/list")
    public ResponseEntity<List<CoffeeEntity>> coffeeList() {
        List<CoffeeEntity> coffees = coffeeService.coffeeList();
        return ResponseEntity.ok(coffees);
    }

    //@PostMapping("/save")
    //public ResponseEntity<CoffeeEntity> saveCoffee(@RequestBody CoffeeEntity coffeeEntity) {
        //CoffeeEntity savedCoffee = coffeeService.saveCoffee(coffeeEntity);
        //return ResponseEntity.ok(savedCoffee);
    //}

    @PostMapping("/coffees")
    public ResponseEntity<?> saveCoffees(@RequestParam(name="name") String name,
                                         @RequestParam(name = "price") Integer price,
                                         @RequestParam(name = "desc") String description,
                                         @RequestParam(name = "image64") MultipartFile image64){
        try {
            CoffeeEntity coffeeEntity = new CoffeeEntity();
            coffeeEntity.setName(name);
            coffeeEntity.setPrice(price);
            coffeeEntity.setDescription(description);
            coffeeEntity.setImage64(Base64.getEncoder().encodeToString(image64.getBytes()));

            CoffeeEntity savedCoffee = coffeeService.saveCoffee(coffeeEntity);
            return ResponseEntity.ok(savedCoffee);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    //Actualizar un café
    @PutMapping("/coffee/updateCoffee")
    public ResponseEntity<?> updateCoffee(@RequestParam(name = "id_coffee") Integer id_coffee,
                                          @RequestParam(name = "name") String name,
                                          @RequestParam(name = "price") Integer price,
                                          @RequestParam(name = "desc") String description,
                                          @RequestParam(name = "image64") MultipartFile image64) {
        try {
            CoffeeEntity updatedCoffee = new CoffeeEntity();
            updatedCoffee.setName(name);
            updatedCoffee.setPrice(price);
            updatedCoffee.setDescription(description);
            updatedCoffee.setImage64(Base64.getEncoder().encodeToString(image64.getBytes()));

            Optional<CoffeeEntity> results = coffeeService.updateCoffee(id_coffee, updatedCoffee);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    //Eliminar un café por ID
    @DeleteMapping("/coffee/deleteCoffee")
    public ResponseEntity<?> deleteCoffee(@RequestParam(name = "id_coffee") int id_coffee) {
        try {
            boolean deleted = coffeeService.deleteCoffeeById(id_coffee);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }
}

