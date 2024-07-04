package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.service.dto.LoginDto;
import cl.ucm.coffee.service.dto.UserDto;
import cl.ucm.coffee.web.config.JwtUtil;
import cl.ucm.coffee.persitence.entity.UserEntity;
import cl.ucm.coffee.persitence.entity.UserRoleEntity;
import cl.ucm.coffee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        UserEntity userEntity = userService.findByUsername(loginDto.getUsername());
        List<String> roles = userEntity.getRoles().stream().map(UserRoleEntity::getRole).collect(Collectors.toList());

        String jwt = this.jwtUtil.create(loginDto.getUsername(), roles);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        //Restablece el campo "logout_time" a Null cuando el cliente o admin inicia sesión
        if (userEntity != null) {
            userEntity.setLogoutTime(null);
            userService.saveUser(userEntity);
        }

        return ResponseEntity.ok(map);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ya existe");
        }
        userDto.setRole(Collections.singletonList("CLIENT")); // Asigna el rol por defecto como CLIENT
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/lock")
    public ResponseEntity<?> lockUser(@RequestParam String username) {
        userService.lockUser(username);
        return ResponseEntity.ok("Usuario bloqueado");
    }

    @PutMapping("/unlock")
    public ResponseEntity<?> unlockUser(@RequestParam String username) {
        userService.unlockUser(username);
        return ResponseEntity.ok("Usuario desbloqueado");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String username = jwtUtil.getUsername(jwt);
            userService.updateLogoutTime(username);
            return ResponseEntity.ok("Sesión cerrada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<UserDto>> getClients() {
        List<UserDto> clients = userService.getAllClients();
        return ResponseEntity.ok(clients);
    }


}
