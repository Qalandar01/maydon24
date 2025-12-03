package uz.ems.maydon24.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ems.maydon24.models.entity.Area;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    @Query("select a from Area a where a.isDeleted = false")
    Page<Area> findAllActive(Pageable pageable);

    Optional<Area> findByIdAndIsDeletedFalse(Long id);
}