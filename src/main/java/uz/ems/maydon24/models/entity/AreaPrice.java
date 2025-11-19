package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.AreaPriceType;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLRestriction("visibility=true")
@Table(name = "area_price")
public class AreaPrice extends BaseEntity {

    @ManyToOne
    private Area area;

    @Enumerated(EnumType.STRING)
    private AreaPriceType type;

    private Double pricePerHour;
}
