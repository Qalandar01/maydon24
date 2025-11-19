package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ems.maydon24.models.base.BaseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "area_discount")
public class AreaDiscount extends BaseEntity {

    @ManyToOne
    private Area area;        // qaysi maydon uchun chegirma

    private Double discountPercent; // 5.0 → 5%, 10.0 → 10%

    // Agar chegirma faqat ma’lum vaqt oralig‘ida bo‘lishi kerak bo‘lsa:
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalTime fromTime; // optional
    private LocalTime toTime;   // optional

    // Userga bog‘liq bo‘lishi uchun optional
    @ManyToOne
    private User user; // null → barcha foydalanuvchilarga amal qiladi

    // Status
    private Boolean active = true;
}
