package cl.ucm.coffee.service;


import cl.ucm.coffee.persitence.entity.TestimonialsEntity;

import java.util.List;

public interface ITestimonialsService {
    TestimonialsEntity createTestimonial(TestimonialsEntity testimonial);
    List<TestimonialsEntity> findByCoffeeId(int coffeeId);

}
