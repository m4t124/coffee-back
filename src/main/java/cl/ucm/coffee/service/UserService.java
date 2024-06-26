package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.UserEntity;
import cl.ucm.coffee.persitence.entity.UserRoleEntity;
import cl.ucm.coffee.persitence.repository.UserRepository;
import cl.ucm.coffee.persitence.repository.UserRoleRepository;
import cl.ucm.coffee.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return userRepository.existsById(username);
    }

    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEmail(userDto.getEmail());
        userEntity.setLocked(false);
        userEntity.setDisabled(false);

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUsername(userDto.getUsername());
        userRoleEntity.setRole(userDto.getRole());
        userRoleEntity.setGrantedDate(LocalDateTime.now());

        userRepository.save(userEntity);
        userRoleRepository.save(userRoleEntity);

        return userDto;
    }

    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEmail(userDto.getEmail());

        userRepository.save(userEntity);

        return userDto;
    }
}
