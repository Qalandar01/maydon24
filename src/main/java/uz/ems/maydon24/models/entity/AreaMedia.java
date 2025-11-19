package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.MediaType;

@Entity
@Table(name = "area_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AreaMedia extends BaseEntity {

    @ManyToOne
    private Area area;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;   // PHOTO or VIDEO

    private String url;            // yuklangan rasm/video URL
}
