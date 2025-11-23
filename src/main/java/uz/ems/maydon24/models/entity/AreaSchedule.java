package uz.ems.maydon24.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.ems.maydon24.models.base.BaseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLRestriction("visibility=true")
@Table(name = "area_schedule")
public class AreaSchedule extends BaseEntity {

    @ManyToOne
    private Area area;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Boolean booked;       // band qilinganmi

    @ManyToOne
    private User bookedBy;      // kim band qilgan

    // todo agar bron qilgan kishi kemay qolsa shart qoshish kerak
}
