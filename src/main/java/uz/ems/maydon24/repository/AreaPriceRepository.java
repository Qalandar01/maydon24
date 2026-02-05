package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.AreaPrice;
import uz.ems.maydon24.models.enums.AreaPriceType;
import uz.ems.maydon24.repository.projection.AreaPriceProjection;

import java.util.List;
import java.util.Optional;

public interface AreaPriceRepository extends JpaRepository<AreaPrice, Long> {
    List<AreaPrice> findAllByAreaIdAndIsDeletedFalse(Long areaId);

    Optional<AreaPrice> findByIdAndAreaIdAndIsDeletedFalse(Long id, Long areaId);

    Optional<AreaPrice> findByAreaIdAndTypeAndIsDeletedFalse(Long areaId, AreaPriceType type);

    @Query(value = """
            select ap.id as "id",
                   ap.area_id as "areaId",
                   ap.type as "type",
                   ap.price_per_hour as "pricePerHour"
            from area_price ap
            where ap.is_deleted = false
              and ap.area_id = :areaId
            order by ap.id desc
            """,
            nativeQuery = true)
    List<AreaPriceProjection> findAllProjectedByAreaId(@Param("areaId") Long areaId);

    @Query(value = """
            select ap.id as "id",
                   ap.area_id as "areaId",
                   ap.type as "type",
                   ap.price_per_hour as "pricePerHour"
            from area_price ap
            where ap.id = :id
              and ap.area_id = :areaId
              and ap.is_deleted = false
            """,
            nativeQuery = true)
    Optional<AreaPriceProjection> findProjectedByIdAndAreaId(@Param("id") Long id, @Param("areaId") Long areaId);
}
