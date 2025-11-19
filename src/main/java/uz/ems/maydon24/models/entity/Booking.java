package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.BookingStatus;
import uz.ems.maydon24.models.enums.BookingType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {

    @ManyToOne
    private Area area;        // qaysi maydon bron qilindi

    @ManyToOne
    private User user;        // kim bron qildi (ENG TO'G'RISI)

    private LocalDate date;   // bron qilingan sana

    private LocalTime fromTime; // boshlanish
    private LocalTime toTime;   // tugash

    @Enumerated(EnumType.STRING)
    private BookingType type; // DAY / NIGHT / WEEKEND

    private Double totalPrice; // jami summasi

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // PENDING / CONFIRMED / PAID / CANCELED
}
