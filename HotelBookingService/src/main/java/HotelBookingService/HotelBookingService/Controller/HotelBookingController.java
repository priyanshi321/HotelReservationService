package HotelBookingService.HotelBookingService.Controller;

import HotelBookingService.HotelBookingService.Entity.HotelBooking;
import HotelBookingService.HotelBookingService.Service.HotelBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Validated
public class HotelBookingController {

    @Autowired
    private HotelBookingService hotelBookingService;

    @PostMapping
    public HotelBooking createBooking(@RequestBody @Valid HotelBooking booking) {
        return hotelBookingService.createBooking(booking);
    }

    @DeleteMapping("/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        return hotelBookingService.cancelBooking(bookingId);
    }

    @GetMapping
    public List<HotelBooking> getAllBookings() {
        return hotelBookingService.getAllBookings();
    }

    @GetMapping("/{bookingId}")
    public HotelBooking getBookingById(@PathVariable Long bookingId) {
        return hotelBookingService.getBookingById(bookingId);
    }

    @PutMapping("/{bookingId}")
    public HotelBooking updateBooking(@PathVariable Long bookingId, @RequestBody @Valid HotelBooking updatedBooking) {
        return hotelBookingService.updateBooking(bookingId, updatedBooking);
    }
}
