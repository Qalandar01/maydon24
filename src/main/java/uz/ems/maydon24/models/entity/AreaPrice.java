package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.AreaPriceType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "area_price")
public class AreaPrice extends BaseEntity {

    @ManyToOne
    private Area area;

    @Enumerated(EnumType.STRING)
    private AreaPriceType type;

    private Double pricePerHour;
}
