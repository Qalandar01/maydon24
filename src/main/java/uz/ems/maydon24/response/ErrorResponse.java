package uz.ems.maydon24.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private boolean success;
    private String message;
    private ErrorCode errorCode;
}
