package uz.ems.maydon24.exeption;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String message;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
