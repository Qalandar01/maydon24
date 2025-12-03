package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.ems.maydon24.models.base.BaseEntity;

@Entity
@Table(name = "area_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaMedia extends BaseEntity {

    @ManyToOne
    private Area area;

    private String contentType;

    @Column(columnDefinition = "BYTEA")
    private byte[] dataPhoto;
}
