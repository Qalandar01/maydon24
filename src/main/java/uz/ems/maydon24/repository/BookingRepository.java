package uz.ems.maydon24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ems.maydon24.models.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}