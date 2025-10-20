package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ems.maydon24.models.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
