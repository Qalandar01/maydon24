package uz.ems.maydon24.models.dto.request;

import lombok.*;
import uz.ems.maydon24.models.enums.AreaType;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaCreateDto {
    private String name;
    private String description;
    private String phoneNumber;
    private Integer height;
    private Integer width;
    private AreaType areaType;
    private List<Long> mediaIds;
}
