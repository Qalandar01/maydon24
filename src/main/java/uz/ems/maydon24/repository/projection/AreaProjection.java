package uz.ems.maydon24.repository.projection;

public interface AreaProjection {
    Long getId();

    String getName();

    String getDescription();

    String getPhoneNumber();

    String getAddress();

    Double getLatitude();

    Double getLongitude();

    Boolean getVisibility();

    Integer getHeight();

    Integer getWidth();

    String getAreaType();
}
