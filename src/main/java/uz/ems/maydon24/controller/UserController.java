package uz.ems.maydon24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.ems.maydon24.models.dto.request.PageDto;
import uz.ems.maydon24.models.dto.response.Response;
import uz.ems.maydon24.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/")
public class UserController {
    private final UserService userService;

    @GetMapping("/getAll")
    public Response getAll(PageDto dto) {
        return userService.getAll(PageRequest.of(dto.getPage(), dto.getSize()));
    }
}
