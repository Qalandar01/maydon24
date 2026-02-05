package uz.ems.maydon24.models.dto.request;

import lombok.Data;
import uz.ems.maydon24.models.enums.AreaFeatureType;

@Data
public class AreaFeatureUpsertDto {
    private AreaFeatureType featureName;
    private Boolean available;
}
