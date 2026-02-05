package uz.ems.maydon24.models.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.ems.maydon24.models.enums.AreaPriceType;

@Data
@Builder
public class AreaPriceResponseDto {
    private Long id;
    private Long areaId;
    private AreaPriceType type;
    private Double pricePerHour;
}
