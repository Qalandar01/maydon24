package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uz.ems.maydon24.models.entity.AreaReview;
import uz.ems.maydon24.repository.projection.AreaReviewProjection;

import java.util.List;
import java.util.Optional;

public interface AreaReviewRepository extends JpaRepository<AreaReview, Long> {
    List<AreaReview> findAllByAreaIdAndIsDeletedFalse(Long areaId);

    Optional<AreaReview> findByIdAndAreaIdAndIsDeletedFalse(Long id, Long areaId);

    Optional<AreaReview> findByAreaIdAndUserIdAndParentReviewIdAndIsDeletedFalse(
            Long areaId,
            Long userId,
            Long parentReviewId
    );

    @Query(value = """
            select r.id as "id",
                   r.area_id as "areaId",
                   r.user_id as "userId",
                   r.rating as "rating",
                   r.comment as "comment",
                   r.is_verified as "verified",
                   r.parent_review_id as "parentReviewId"
            from area_reviews r
            where r.is_deleted = false
              and r.area_id = :areaId
            order by r.id desc
            """,
            nativeQuery = true)
    List<AreaReviewProjection> findAllProjectedByAreaId(@Param("areaId") Long areaId);

    @Query(value = """
            select r.id as "id",
                   r.area_id as "areaId",
                   r.user_id as "userId",
                   r.rating as "rating",
                   r.comment as "comment",
                   r.is_verified as "verified",
                   r.parent_review_id as "parentReviewId"
            from area_reviews r
            where r.id = :id
              and r.area_id = :areaId
              and r.is_deleted = false
            """,
            nativeQuery = true)
    Optional<AreaReviewProjection> findProjectedByIdAndAreaId(@Param("id") Long id, @Param("areaId") Long areaId);
}
