package uz.ems.maydon24.config.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.ems.maydon24.models.entity.Role;
import uz.ems.maydon24.models.enums.Roles;
import uz.ems.maydon24.repository.RoleRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DefaultBase implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        createDefaultRoles();
    }

    private void createDefaultRoles() {
        // Default roles
        Set<Roles> defaultRoles = new HashSet<>(Arrays.asList(
                Roles.ROLE_USER,
                Roles.ROLE_ADMIN
        ));

        for (Roles roleName : defaultRoles) {
            boolean exists = roleRepository.findByName(roleName).isPresent();
            if (!exists) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();
                roleRepository.save(role);
                System.out.println("✅ Created default role: " + roleName);
            }
        }
    }
}
