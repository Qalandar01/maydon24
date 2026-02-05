package uz.ems.maydon24.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.Area;
import uz.ems.maydon24.repository.projection.AreaProjection;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    @Query("select a from Area a where a.isDeleted = false")
    Page<Area> findAllActive(Pageable pageable);

    Optional<Area> findByIdAndIsDeletedFalse(Long id);

    Optional<Area> findByNameAndPhoneNumberAndIsDeletedFalse(String name, String phoneNumber);

    @Query(value = """
            select a.id as "id",
                   a.name as "name",
                   a.description as "description",
                   a.phone_number as "phoneNumber",
                   a.address as "address",
                   a.latitude as "latitude",
                   a.longitude as "longitude",
                   a.visibility as "visibility",
                   a.height as "height",
                   a.width as "width",
                   a.area_type as "areaType"
            from areas a
            where a.is_deleted = false
            order by a.id desc
            """,
            countQuery = """
                    select count(1)
                    from areas a
                    where a.is_deleted = false
                    """,
            nativeQuery = true)
    Page<AreaProjection> findAllActiveProjected(Pageable pageable);

    @Query(value = """
            select a.id as "id",
                   a.name as "name",
                   a.description as "description",
                   a.phone_number as "phoneNumber",
                   a.address as "address",
                   a.latitude as "latitude",
                   a.longitude as "longitude",
                   a.visibility as "visibility",
                   a.height as "height",
                   a.width as "width",
                   a.area_type as "areaType"
            from areas a
            where a.id = :id
              and a.is_deleted = false
            """,
            nativeQuery = true)
    Optional<AreaProjection> findProjectedById(@Param("id") Long id);
}
