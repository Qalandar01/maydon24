package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ems.maydon24.models.entity.Role;
import uz.ems.maydon24.models.enums.RoleName;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

    Set<Role> findAllByNameIn(Set<RoleName> names);
}
