package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.AreaFeature;
import uz.ems.maydon24.models.enums.AreaFeatureType;
import uz.ems.maydon24.repository.projection.AreaFeatureProjection;

import java.util.List;
import java.util.Optional;

public interface AreaFeatureRepository extends JpaRepository<AreaFeature, Long> {
    List<AreaFeature> findAllByAreaIdAndIsDeletedFalse(Long areaId);

    Optional<AreaFeature> findByIdAndAreaIdAndIsDeletedFalse(Long id, Long areaId);

    Optional<AreaFeature> findByAreaIdAndFeatureNameAndIsDeletedFalse(Long areaId, AreaFeatureType featureName);

    @Query(value = """
            select af.id as "id",
                   af.area_id as "areaId",
                   af.feature_name as "featureName",
                   af.available as "available"
            from area_features af
            where af.is_deleted = false
              and af.area_id = :areaId
            order by af.id desc
            """,
            nativeQuery = true)
    List<AreaFeatureProjection> findAllProjectedByAreaId(@Param("areaId") Long areaId);

    @Query(value = """
            select af.id as "id",
                   af.area_id as "areaId",
                   af.feature_name as "featureName",
                   af.available as "available"
            from area_features af
            where af.id = :id
              and af.area_id = :areaId
              and af.is_deleted = false
            """,
            nativeQuery = true)
    Optional<AreaFeatureProjection> findProjectedByIdAndAreaId(@Param("id") Long id, @Param("areaId") Long areaId);
}
