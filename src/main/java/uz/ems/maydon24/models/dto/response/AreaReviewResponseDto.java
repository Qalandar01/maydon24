package uz.ems.maydon24.models.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AreaReviewResponseDto {
    private Long id;
    private Long areaId;
    private Long userId;
    private Integer rating;
    private String comment;
    private Boolean verified;
    private Long parentReviewId;
}
