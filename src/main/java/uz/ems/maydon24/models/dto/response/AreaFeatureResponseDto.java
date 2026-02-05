package uz.ems.maydon24.models.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.ems.maydon24.models.enums.AreaFeatureType;

@Data
@Builder
public class AreaFeatureResponseDto {
    private Long id;
    private Long areaId;
    private AreaFeatureType featureName;
    private Boolean available;
}
