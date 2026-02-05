package uz.ems.maydon24.repository.projection;

public interface AreaReviewProjection {
    Long getId();

    Long getAreaId();

    Long getUserId();

    Integer getRating();

    String getComment();

    Boolean getVerified();

    Long getParentReviewId();
}
