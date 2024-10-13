package HotelBookingService.HotelBookingService.Controller;

import HotelBookingService.HotelBookingService.Entity.HotelBooking;
import HotelBookingService.HotelBookingService.Service.HotelBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HotelBooking> createBooking(@RequestBody @Valid HotelBooking booking) {
        HotelBooking createdBooking = hotelBookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        String message = hotelBookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<HotelBooking>> getAllBookings() {
        List<HotelBooking> bookings = hotelBookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<HotelBooking> getBookingById(@PathVariable Long bookingId) {
        HotelBooking booking = hotelBookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<HotelBooking> updateBooking(@PathVariable Long bookingId, @RequestBody @Valid HotelBooking updatedBooking) {
        HotelBooking updated = hotelBookingService.updateBooking(bookingId, updatedBooking);
        return ResponseEntity.ok(updated);
    }

}
