package cl.ucm.coffee.persitence.repository;

import cl.ucm.coffee.persitence.entity.UserRoleEntity;
import cl.ucm.coffee.persitence.entity.UserRoleId;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRoleEntity, UserRoleId> {
}
