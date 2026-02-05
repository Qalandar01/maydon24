package uz.ems.maydon24.models.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AreaDiscountUpsertDto {
    private Double discountPercent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime fromTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime toTime;
    private Long userId;
    private Boolean active;
}
