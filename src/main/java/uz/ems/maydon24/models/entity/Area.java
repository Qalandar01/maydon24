package uz.ems.maydon24.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.AreaType;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "areas")
@SQLRestriction("visibility=true")
public class Area extends BaseEntity {

    private String name;
    private String description;
    private String phoneNumber;
    private Integer height;
    private Integer width;
    @Enumerated(EnumType.STRING)
    private AreaType areaType;


}
