package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.AreaFeatureType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "area_features")
@SQLRestriction("visibility=true")
public class AreaFeature extends BaseEntity {

    @ManyToOne
    private Area area;

    @Enumerated(EnumType.STRING)
    private AreaFeatureType featureName;

    private Boolean available;
}
