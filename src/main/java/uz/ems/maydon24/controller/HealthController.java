package uz.ems.maydon24.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class HealthController {
    private final String healthSecret;

    public HealthController(@Value("${HEALTH_SECRET:}") String healthSecret) {
        this.healthSecret = healthSecret;
    }

    @GetMapping("/health")
    public Map<String, String> health(
            @RequestHeader(value = "X-Health-Secret", required = false) String secret
    ) {
        if (healthSecret != null && !healthSecret.isBlank() && !healthSecret.equals(secret)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return Map.of("status", "UP");
    }
}
