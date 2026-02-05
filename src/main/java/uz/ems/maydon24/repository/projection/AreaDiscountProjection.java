package uz.ems.maydon24.repository.projection;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AreaDiscountProjection {
    Long getId();

    Long getAreaId();

    Double getDiscountPercent();

    LocalDate getFromDate();

    LocalDate getToDate();

    LocalTime getFromTime();

    LocalTime getToTime();

    Long getUserId();

    Boolean getActive();
}
