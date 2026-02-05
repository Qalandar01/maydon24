package uz.ems.maydon24.models.dto.request;

import lombok.Data;
import uz.ems.maydon24.models.enums.AreaPriceType;

@Data
public class AreaPriceUpsertDto {
    private AreaPriceType type;
    private Double pricePerHour;
}
