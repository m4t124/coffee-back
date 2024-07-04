package cl.ucm.coffee.persitence.repository;

import cl.ucm.coffee.persitence.entity.UserEntity;
import cl.ucm.coffee.persitence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.role = :role")
    List<UserEntity> findByRolesRole(@Param("role") String role);
}
