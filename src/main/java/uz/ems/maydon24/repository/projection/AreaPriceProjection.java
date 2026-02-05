package uz.ems.maydon24.repository.projection;

public interface AreaPriceProjection {
    Long getId();

    Long getAreaId();

    String getType();

    Double getPricePerHour();
}
