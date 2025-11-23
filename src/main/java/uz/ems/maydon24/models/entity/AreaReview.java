package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ems.maydon24.models.base.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "area_reviews")
public class AreaReview extends BaseEntity {

    @ManyToOne
    private Area area;      // qaysi maydonga review

    @ManyToOne
    private User user;      // kim yozdi

    @Column(nullable = false)
    private Integer rating; // 1–5

    @Column(length = 2000)
    private String comment; // izoh

    private Boolean isVerified = false; // admin tasdiqladi

    @ManyToOne
    private AreaReview parentReview; // agar reply bo‘lsa

}
