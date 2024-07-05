package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.TestimonialsEntity;
import cl.ucm.coffee.service.ITestimonialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialsController {

    @Autowired
    private ITestimonialsService testimonialsService;

    @PostMapping("/create") //Crear testimonio
    public ResponseEntity<?> create(@RequestBody TestimonialsEntity testimonial) {
        try {
            TestimonialsEntity createdTestimonial = testimonialsService.createTestimonial(testimonial);
            return ResponseEntity.ok(createdTestimonial);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el testimonio: " + e.getMessage());
        }
    }

    @GetMapping("/findByCoffeeId") //Obtener testimonios por ID de café
    public ResponseEntity<?> findByCoffeeId(@RequestParam(name = "coffeeId") int coffeeId) {
        try {
            List<TestimonialsEntity> testimonials = testimonialsService.findByCoffeeId(coffeeId);
            return ResponseEntity.ok(testimonials);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al encontrar testimonios por ID de café: " + e.getMessage());
        }
    }
}
