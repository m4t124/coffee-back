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
import java.util.List;
import java.util.stream.Collectors;

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

    public UserEntity findByUsername(String username) {
        return userRepository.findById(username).orElse(null);
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEmail(userDto.getEmail());
        userEntity.setLocked(false);
        userEntity.setDisabled(false);

        if (userDto.getRole() != null && !userDto.getRole().isEmpty()) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUsername(userDto.getUsername());
            userRoleEntity.setRole(userDto.getRole().get(0));
            userRoleEntity.setGrantedDate(LocalDateTime.now());

            userRepository.save(userEntity);
            userRoleRepository.save(userRoleEntity);
        } else {
            throw new IllegalArgumentException("El rol no puede estar vacio");
        }

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

    public void lockUser(String username) {
        UserEntity userEntity = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userEntity.setLocked(true);
        userRepository.save(userEntity);
    }

    public void unlockUser(String username) {
        UserEntity userEntity = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userEntity.setLocked(false);
        userRepository.save(userEntity);
    }

    public void updateLogoutTime(String username) {
        UserEntity userEntity = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userEntity.setLogoutTime(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    public List<UserDto> getAllClients() {
        List<UserEntity> clients = userRepository.findByRolesRole("CLIENT");
        return clients.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private UserDto convertToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setEmail(userEntity.getEmail());
        userDto.setRole(userEntity.getRoles().stream().map(UserRoleEntity::getRole).collect(Collectors.toList()));
        return userDto;
    }
}
