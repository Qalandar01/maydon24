package uz.ems.maydon24.models.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AreaScheduleUpsertDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Boolean booked;
    private Long bookedById;
}
