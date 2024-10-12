package HotelBookingService.HotelBookingService.Service;
import HotelBookingService.HotelBookingService.Entity.HotelBooking;
import jakarta.validation.Valid;

import java.util.List;

public interface HotelBookingService {

    HotelBooking createBooking(HotelBooking booking);

    String cancelBooking(long bookingId);

    List<HotelBooking> getAllBookings();

    HotelBooking getBookingById(long bookingId);
    HotelBooking updateBooking(Long bookingId, @Valid HotelBooking updatedBooking);
}
