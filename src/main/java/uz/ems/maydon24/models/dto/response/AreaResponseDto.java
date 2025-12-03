package uz.ems.maydon24.models.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.ems.maydon24.models.enums.AreaType;

import java.util.List;

@Data
@Builder
public class AreaResponseDto {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private Integer height;
    private Integer width;
    private AreaType areaType;

    private List<String> media;
}
