package uz.ems.maydon24.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import uz.ems.maydon24.exeption.CustomException;
import uz.ems.maydon24.mapper.UserMapper;
import uz.ems.maydon24.models.dto.request.LoginDto;
import uz.ems.maydon24.models.dto.request.RegisterDto;
import uz.ems.maydon24.models.dto.request.UserDto;
import uz.ems.maydon24.models.dto.response.ErrorResponse;
import uz.ems.maydon24.models.dto.response.Response;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.models.enums.Roles;
import uz.ems.maydon24.repository.UserRepository;
import uz.ems.maydon24.service.AuthService;
import uz.ems.maydon24.utils.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Response login(LoginDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("USER NOT FOUND"));

        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(
                    authentication
            );
        } catch (Exception e) {
            var error = ErrorResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(" INVALID PASSWORD")
                    .build();
            return Response.error(error);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        UserDto userDto = userMapper.toDto(user);
        return Response.success(userDto, token);
    }

    @Override
    public Response updateRole(Long id, Roles role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "USER NOT FOUND: ID=>%d".formatted(id)));
        user.setRole(role);
        userRepository.save(user);
        return Response.success();
    }


    @Override
    public Response register(RegisterDto dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElse(null);
        String token = null;
        if (user == null) {
            User entity = userMapper.toEntity(dto);
            entity.setRole(Roles.ROLE_USER);
            User save = userRepository.save(entity);
            token = jwtUtil.generateToken(save.getUsername());

            return Response.success(userMapper.toDto(save), token);
        }
        var error = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("USER ALREADY EXISTS")
                .build();
        return Response.error(error);
    }
}
