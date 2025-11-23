package uz.ems.maydon24.service;


import org.springframework.stereotype.Component;
import uz.ems.maydon24.models.dto.request.LoginDto;
import uz.ems.maydon24.models.dto.request.RegisterDto;
import uz.ems.maydon24.models.dto.response.Response;
import uz.ems.maydon24.models.enums.Roles;

@Component
public interface AuthService {
    Response login(LoginDto dto);

    Response updateRole(Long id, Roles role);

    Response register(RegisterDto dto);
}
