package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.AreaSchedule;
import uz.ems.maydon24.repository.projection.AreaScheduleProjection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AreaScheduleRepository extends JpaRepository<AreaSchedule, Long> {
    List<AreaSchedule> findAllByAreaIdAndIsDeletedFalse(Long areaId);

    Optional<AreaSchedule> findByIdAndAreaIdAndIsDeletedFalse(Long id, Long areaId);

    Optional<AreaSchedule> findByAreaIdAndDateAndStartTimeAndEndTimeAndIsDeletedFalse(
            Long areaId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );

    @Query(value = """
            select s.id as "id",
                   s.area_id as "areaId",
                   s.date as "date",
                   s.start_time as "startTime",
                   s.end_time as "endTime",
                   s.booked as "booked",
                   s.booked_by_id as "bookedById"
            from area_schedule s
            where s.is_deleted = false
              and s.area_id = :areaId
            order by s.id desc
            """,
            nativeQuery = true)
    List<AreaScheduleProjection> findAllProjectedByAreaId(@Param("areaId") Long areaId);

    @Query(value = """
            select s.id as "id",
                   s.area_id as "areaId",
                   s.date as "date",
                   s.start_time as "startTime",
                   s.end_time as "endTime",
                   s.booked as "booked",
                   s.booked_by_id as "bookedById"
            from area_schedule s
            where s.id = :id
              and s.area_id = :areaId
              and s.is_deleted = false
            """,
            nativeQuery = true)
    Optional<AreaScheduleProjection> findProjectedByIdAndAreaId(@Param("id") Long id, @Param("areaId") Long areaId);
}
