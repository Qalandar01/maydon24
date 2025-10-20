package uz.ems.maydon24.models.dto.response;

public record UserRes (
        Long id,
        Long telegramId,
        String fullName,
        String telegramUsername,
        String phone,
        Boolean isAdmin
){}
