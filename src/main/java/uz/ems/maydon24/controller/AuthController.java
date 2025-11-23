package uz.ems.maydon24.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.ems.maydon24.models.dto.request.LoginDto;
import uz.ems.maydon24.models.dto.request.RegisterDto;
import uz.ems.maydon24.models.dto.response.Response;
import uz.ems.maydon24.models.enums.Roles;
import uz.ems.maydon24.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "AUTH-CONTROLLER")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginDto dto) {
        return authService.login(dto);
    }

    @PutMapping("/updateRole")
    public Response updateRole(@RequestParam("id") Long id,
                               @RequestParam("role") Roles role) {
        return authService.updateRole(id, role);
    }

    @PostMapping("/register")
    public Response register(@RequestBody RegisterDto dto) {
        return authService.register(dto);
    }
}
