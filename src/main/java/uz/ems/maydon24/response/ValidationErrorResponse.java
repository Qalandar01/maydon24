package uz.ems.maydon24.response;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationErrorResponse {
    private boolean success;
    private String message;
    private Map<String, String> fieldErrors;
}
