package uz.ems.maydon24.models.dto.response;

import lombok.Builder;

@Builder
public record UserRes (
        Long id,
        Long telegramId,
        String fullName,
        String telegramUsername,
        String phoneNumber,
        Boolean isAdmin
){}
