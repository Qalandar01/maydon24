package uz.ems.maydon24.repository.projection;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AreaScheduleProjection {
    Long getId();

    Long getAreaId();

    LocalDate getDate();

    LocalTime getStartTime();

    LocalTime getEndTime();

    Boolean getBooked();

    Long getBookedById();
}
