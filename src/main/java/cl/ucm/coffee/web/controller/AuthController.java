package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.service.dto.LoginDto;
import cl.ucm.coffee.service.dto.UserDto;
import cl.ucm.coffee.web.config.JwtUtil;
import cl.ucm.coffee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        String jwt = this.jwtUtil.create(loginDto.getUsername());
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ya existe");
        }
        userDto.setRole("CLIENT"); // Asigna el rol por defecto como CLIENT
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }
}

