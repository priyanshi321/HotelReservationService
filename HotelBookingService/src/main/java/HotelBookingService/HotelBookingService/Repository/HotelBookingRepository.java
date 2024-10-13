package HotelBookingService.HotelBookingService.Repository;
import HotelBookingService.HotelBookingService.Entity.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findByHotelIdAndRoomTypeAndCheckinDateLessThanEqualAndCheckoutDateGreaterThanEqual(
            Long hotelId, String roomType, LocalDateTime checkoutDate, LocalDateTime checkinDate);
    List<HotelBooking> findByBookingStatusNot(String status);
}

