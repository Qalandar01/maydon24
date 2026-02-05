package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.AreaDiscount;
import uz.ems.maydon24.repository.projection.AreaDiscountProjection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AreaDiscountRepository extends JpaRepository<AreaDiscount, Long> {
    List<AreaDiscount> findAllByAreaIdAndIsDeletedFalse(Long areaId);

    Optional<AreaDiscount> findByIdAndAreaIdAndIsDeletedFalse(Long id, Long areaId);

    Optional<AreaDiscount> findByAreaIdAndUserIdAndFromDateAndToDateAndFromTimeAndToTimeAndIsDeletedFalse(
            Long areaId,
            Long userId,
            LocalDate fromDate,
            LocalDate toDate,
            LocalTime fromTime,
            LocalTime toTime
    );

    @Query(value = """
            select ad.id as "id",
                   ad.area_id as "areaId",
                   ad.discount_percent as "discountPercent",
                   ad.from_date as "fromDate",
                   ad.to_date as "toDate",
                   ad.from_time as "fromTime",
                   ad.to_time as "toTime",
                   ad.user_id as "userId",
                   ad.active as "active"
            from area_discount ad
            where ad.is_deleted = false
              and ad.area_id = :areaId
            order by ad.id desc
            """,
            nativeQuery = true)
    List<AreaDiscountProjection> findAllProjectedByAreaId(@Param("areaId") Long areaId);

    @Query(value = """
            select ad.id as "id",
                   ad.area_id as "areaId",
                   ad.discount_percent as "discountPercent",
                   ad.from_date as "fromDate",
                   ad.to_date as "toDate",
                   ad.from_time as "fromTime",
                   ad.to_time as "toTime",
                   ad.user_id as "userId",
                   ad.active as "active"
            from area_discount ad
            where ad.id = :id
              and ad.area_id = :areaId
              and ad.is_deleted = false
            """,
            nativeQuery = true)
    Optional<AreaDiscountProjection> findProjectedByIdAndAreaId(@Param("id") Long id, @Param("areaId") Long areaId);
}
