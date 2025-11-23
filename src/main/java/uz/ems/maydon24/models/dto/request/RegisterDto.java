package uz.ems.maydon24.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
    private Long telegramId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
