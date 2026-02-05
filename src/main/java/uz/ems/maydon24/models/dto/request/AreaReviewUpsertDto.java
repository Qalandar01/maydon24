package uz.ems.maydon24.models.dto.request;

import lombok.Data;

@Data
public class AreaReviewUpsertDto {
    private Long userId;
    private Integer rating;
    private String comment;
    private Boolean verified;
    private Long parentReviewId;
}
