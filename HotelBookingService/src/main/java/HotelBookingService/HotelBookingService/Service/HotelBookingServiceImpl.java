package HotelBookingService.HotelBookingService.Service;
import HotelBookingService.HotelBookingService.Entity.HotelBooking;
import HotelBookingService.HotelBookingService.Repository.HotelBookingRepository;
import HotelDetailsService.HotelDetailsService.Entity.HotelDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class HotelBookingServiceImpl implements HotelBookingService {

    @Autowired
    private HotelBookingRepository hotelBookingRepository;
    @Autowired
    private RestTemplate restTemplate;
    private final String HOTEL_SERVICE_URL = "http://localhost:8083/hotels/";

    @Cacheable(value = "hotelDetailsCache", key = "#hotelId")
    public HotelDetails getHotelDetails(Long hotelId) {
        return restTemplate.getForObject(HOTEL_SERVICE_URL + hotelId, HotelDetails.class);
    }
    public HotelBooking createBooking(@Valid HotelBooking hotelBooking) {
        HotelDetails hotel = getHotelDetails(hotelBooking.getHotelId());

        if (!"Available".equals(hotel.getStatus())) {
            throw new IllegalArgumentException("Hotel is not available for booking.");
        }

        Map<String, Integer> roomAvailability = hotel.getRoomAvailability();
        String requestedRoomType = hotelBooking.getRoomType();
        int roomsToBook = hotelBooking.getNumberOfRooms();

        // Check if the requested room type exists
        if (!roomAvailability.containsKey(requestedRoomType)) {
            throw new IllegalArgumentException("Requested room type does not exist.");
        }

        // Check if there are enough rooms available for the requested type
        int availableRooms = roomAvailability.get(requestedRoomType);
        if (availableRooms < roomsToBook) {
            throw new IllegalArgumentException("Not enough rooms available for " + requestedRoomType + ". Please select another room type.");
        }

        // Set booking details
        hotelBooking.setBookingId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        hotelBooking.setPaymentStatus("Paid");

        // Save the booking
        HotelBooking savedBooking = hotelBookingRepository.save(hotelBooking);

        // Update room availability after booking
        roomAvailability.put(requestedRoomType, availableRooms - roomsToBook);
        hotel.setRoomAvailability(roomAvailability);
        restTemplate.put(HOTEL_SERVICE_URL + hotel.getHotelId(), hotel);

        // Evict hotel details cache if necessary
        evictHotelDetailsCache(hotel.getHotelId());

        return savedBooking;
    }


    @CacheEvict(value = "hotelDetailsCache", key = "#hotelId")
    public void evictHotelDetailsCache(Long hotelId) {

    }

    @CacheEvict(value = {"bookingCache", "allBookingsCache"}, allEntries = true)
    public void evictAllBookingsCache() {}
    @Override
    @CacheEvict(value = {"bookingCache", "allBookingsCache"}, key = "#bookingId", allEntries = true)
    public String cancelBooking(long bookingId) {
        Optional<HotelBooking> booking = hotelBookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            hotelBookingRepository.delete(booking.get());
            return "Booking cancelled successfully.";
        }
        return "Booking not found.";
    }

    @Override
    @Cacheable(value = "allBookingsCache")
    public List<HotelBooking> getAllBookings() {
        return hotelBookingRepository.findAll();
    }

    @Override
    @Cacheable(value = "bookingCache", key = "#bookingId")
    public HotelBooking getBookingById(long bookingId) {
        return hotelBookingRepository.findById(bookingId).orElse(null);
    }
    public HotelBooking updateBooking(Long bookingId, @Valid HotelBooking updatedBooking) {
        HotelBooking existingBooking = hotelBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        HotelDetails hotel = getHotelDetails(existingBooking.getHotelId());
        Map<String, Integer> roomAvailability = hotel.getRoomAvailability();
        String requestedRoomType = updatedBooking.getRoomType();
        int roomsToBook = updatedBooking.getNumberOfRooms();

        if (!roomAvailability.containsKey(requestedRoomType)) {
            throw new IllegalArgumentException("Requested room type does not exist.");
        }

        // Get current booking rooms
        int currentBookingRooms = existingBooking.getNumberOfRooms();

        // Check for overlapping bookings, excluding the current one
        List<HotelBooking> overlappingBookings = hotelBookingRepository.findByHotelIdAndRoomTypeAndCheckinDateLessThanEqualAndCheckoutDateGreaterThanEqual(
                existingBooking.getHotelId(), requestedRoomType, updatedBooking.getCheckinDate(), updatedBooking.getCheckoutDate());

        if (!overlappingBookings.isEmpty() && !overlappingBookings.contains(existingBooking)) {
            throw new IllegalArgumentException("Requested room type is not available for the selected dates.");
        }

        // Update the existing booking fields
        existingBooking.setUsername(updatedBooking.getUsername());
        existingBooking.setPhoneNumber(updatedBooking.getPhoneNumber());
        existingBooking.setEmailId(updatedBooking.getEmailId());
        existingBooking.setCheckinDate(updatedBooking.getCheckinDate());
        existingBooking.setCheckoutDate(updatedBooking.getCheckoutDate());
        existingBooking.setRoomType(requestedRoomType);
        existingBooking.setNumberOfRooms(roomsToBook);

        // Save the updated booking
        HotelBooking savedBooking = hotelBookingRepository.save(existingBooking);

        // Calculate available rooms
        int availableRooms = roomAvailability.get(requestedRoomType);

        // Update room availability
        roomAvailability.put(requestedRoomType, availableRooms + currentBookingRooms - roomsToBook);
        hotel.setRoomAvailability(roomAvailability);

        // Update hotel details service
        restTemplate.put(HOTEL_SERVICE_URL + hotel.getHotelId(), hotel);

        // Evict caches
        evictHotelDetailsCache(hotel.getHotelId());
        evictAllBookingsCache();

        return savedBooking;
    }


}
