package cl.ucm.coffee.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String role;
}
