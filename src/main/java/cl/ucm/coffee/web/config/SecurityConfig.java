package cl.ucm.coffee.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/coffee/list").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/coffee/search").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/coffee/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/coffee/updateCoffee").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/coffee/deleteCoffee").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/testimonials/create").hasRole("CLIENT")
                .requestMatchers(HttpMethod.GET, "/api/testimonials/findByCoffeeId").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/create").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/auth/update").hasRole("CLIENT")
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/auth/lock").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/auth/unlock").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/auth/clientes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
