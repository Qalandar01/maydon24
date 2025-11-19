package uz.ems.maydon24.models.dto.response;

import lombok.Builder;

@Builder
public record LoginRes (
        String token,
        UserRes userRes
){}
