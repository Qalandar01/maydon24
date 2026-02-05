package uz.ems.maydon24.models.dto.request;

import lombok.Data;
import uz.ems.maydon24.models.enums.AreaType;

@Data
public class AreaUpdateDto {
    private String name;
    private String description;
    private String phoneNumber;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean visibility;
    private Integer height;
    private Integer width;
    private AreaType areaType;
}
