package uz.ems.maydon24.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.mapper.UserMapper;
import uz.ems.maydon24.models.dto.response.Response;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.repository.UserRepository;
import uz.ems.maydon24.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Response getAll(Pageable pageable) {

        Page<User> page = userRepository.findAll(pageable);
        if (!page.isEmpty()) {
            return Response.builder()
                    .data(page.map(userMapper::toDto))
                    .build();
        }
        return Response.success();
    }
}
