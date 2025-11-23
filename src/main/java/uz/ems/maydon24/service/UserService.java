package uz.ems.maydon24.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import uz.ems.maydon24.models.dto.response.Response;

@Component
public interface UserService {

    Response getAll(Pageable pageable);
}
