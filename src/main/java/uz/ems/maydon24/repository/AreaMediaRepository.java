package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.AreaMedia;
import uz.ems.maydon24.repository.projection.AreaMediaProjection;

import java.util.List;
import java.util.Optional;

public interface AreaMediaRepository extends JpaRepository<AreaMedia, Long> {
    List<AreaMedia> findAllByAreaIdAndIsDeletedFalse(Long areaId);
    @Query(value = """
    select * from area_media a 
    where a.is_deleted=false and a.id=:id
        """,nativeQuery=true)
    Optional<AreaMedia> findByIdAndDeleted(@Param("id") Long id);

    @Query(value = """
            select a.content_type as "contentType",
                   a.data_photo as "dataPhoto"
            from area_media a
            where a.is_deleted = false
              and a.area_id = :areaId
            order by a.id desc
            """,
            nativeQuery = true)
    List<AreaMediaProjection> findAllProjectedByAreaId(@Param("areaId") Long areaId);
}
