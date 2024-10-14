package HotelBookingService.HotelBookingService.Service;
import HotelBookingService.HotelBookingService.Entity.HotelBooking;
import HotelDetailsService.HotelDetailsService.Entity.HotelDetails;
import HotelBookingService.HotelBookingService.Repository.HotelBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import jakarta.validation.Valid;
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

    private HotelDetails getHotelDetails(Long hotelId) {
        try {
            return restTemplate.getForObject(HOTEL_SERVICE_URL + hotelId, HotelDetails.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching hotel details for ID: " + hotelId, e);
        }
    }

    @Override
    public List<HotelBooking> getAllBookings() {
        return hotelBookingRepository.findByBookingStatusNot("Canceled");
    }

    @Override
    public HotelBooking getBookingById(long bookingId) {

        Optional<HotelBooking> bookingOptional = hotelBookingRepository.findById(bookingId);


        if (bookingOptional.isPresent()) {
            HotelBooking booking = bookingOptional.get();
            if ("Canceled".equals(booking.getBookingStatus())) {
                throw new IllegalArgumentException("Booking ID " + bookingId + " no longer exists.");
            }
            return booking;
        } else {

            throw new IllegalArgumentException("Booking ID " + bookingId + " does not exist.");
        }
    }



    @Override
    public HotelBooking createBooking(@Valid HotelBooking hotelBooking) {
        if (hotelBooking.getHotelId() == null || hotelBooking.getRoomType() == null || hotelBooking.getNumberOfRooms() <= 0) {
            throw new IllegalArgumentException("Invalid booking details.");
        }

        HotelDetails hotel = getHotelDetails(hotelBooking.getHotelId());

        if (hotel == null || !"Available".equals(hotel.getStatus())) {
            throw new IllegalArgumentException("Hotel ID " + hotelBooking.getHotelId() + " is not available.");
        }

        String requestedRoomType = hotelBooking.getRoomType();
        int roomsToBook = hotelBooking.getNumberOfRooms();
        Map<String, Integer> roomAvailability = hotel.getRoomAvailability();

        synchronized (this) {
            int availableRooms = roomAvailability.getOrDefault(requestedRoomType, 0);


            if (availableRooms < roomsToBook) {
                throw new IllegalArgumentException("Not enough rooms available for " + requestedRoomType + ". It's full.");
            }


            int updatedAvailability = availableRooms - roomsToBook;

            roomAvailability.put(requestedRoomType, updatedAvailability);
            hotel.setRoomAvailability(roomAvailability);

            hotelBooking.setBookingId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            hotelBooking.setPaymentStatus("Paid");
            hotelBooking.setBookingStatus("Booked");

            HotelBooking savedBooking = hotelBookingRepository.save(hotelBooking);


            try {
                restTemplate.put(HOTEL_SERVICE_URL + hotel.getHotelId(), hotel);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw new RuntimeException("Error updating hotel availability for ID: " + hotel.getHotelId() + ". Response: " + e.getResponseBodyAsString(), e);
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error updating hotel availability for ID: " + hotel.getHotelId(), e);
            }

            return savedBooking;
        }
    }

    @Override
    public HotelBooking updateBooking(Long bookingId, @Valid HotelBooking updatedBooking) {

        HotelBooking existingBooking = hotelBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking ID " + bookingId + " does not exist."));


        if ("Canceled".equals(existingBooking.getBookingStatus())) {
            throw new IllegalArgumentException("Booking ID " + bookingId + " no longer exists.");
        }


        String oldRoomType = existingBooking.getRoomType();
        int oldRoomsToBook = existingBooking.getNumberOfRooms();


        if (!existingBooking.getHotelId().equals(updatedBooking.getHotelId())) {
            throw new IllegalArgumentException("Hotel update available for same hotel type.");
        }


        HotelDetails hotel = getHotelDetails(existingBooking.getHotelId());
        if (hotel == null || !hotel.getRoomAvailability().containsKey(updatedBooking.getRoomType())) {
            throw new IllegalArgumentException("Invalid room type or hotel ID.");
        }

        Map<String, Integer> roomAvailability = hotel.getRoomAvailability();
       
        if (roomAvailability.containsKey(oldRoomType)) {
            roomAvailability.put(oldRoomType, roomAvailability.get(oldRoomType) + oldRoomsToBook);
        }
        String newRoomType = updatedBooking.getRoomType();
        int newRoomsToBook = updatedBooking.getNumberOfRooms();
        int availableRooms = roomAvailability.getOrDefault(newRoomType, 0);
        System.out.println("Available rooms for " + newRoomType + ": " + availableRooms)
        if (availableRooms < newRoomsToBook) {
            throw new IllegalArgumentException("Not enough rooms available for " + newRoomType + ". Only " + availableRooms + " rooms available.");
        }
        roomAvailability.put(newRoomType, availableRooms - newRoomsToBook);

        existingBooking.setRoomType(newRoomType);
        existingBooking.setNumberOfRooms(newRoomsToBook);
        existingBooking.setCheckinDate(updatedBooking.getCheckinDate());
        existingBooking.setCheckoutDate(updatedBooking.getCheckoutDate());

        HotelBooking savedBooking = hotelBookingRepository.save(existingBooking);


        try {
            hotel.setRoomAvailability(roomAvailability);
            restTemplate.put(HOTEL_SERVICE_URL + hotel.getHotelId(), hotel);
        } catch (Exception e) {
            throw new RuntimeException("Error updating hotel availability for ID: " + hotel.getHotelId(), e);
        }

        return savedBooking;
    }

    @Override
    public String cancelBooking(long bookingId) {
        HotelBooking existingBooking = hotelBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking ID " + bookingId + " does not exist."));

        HotelDetails hotel = getHotelDetails(existingBooking.getHotelId());
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel ID " + existingBooking.getHotelId() + " does not exist.");
        }

        Map<String, Integer> roomAvailability = hotel.getRoomAvailability();
        if (roomAvailability != null) {
            roomAvailability.put(existingBooking.getRoomType(), roomAvailability.getOrDefault(existingBooking.getRoomType(), 0) + existingBooking.getNumberOfRooms());
        }

        existingBooking.setBookingStatus("Canceled");
        hotelBookingRepository.save(existingBooking);
        try {
            restTemplate.put(HOTEL_SERVICE_URL + hotel.getHotelId(), hotel);
        } catch (Exception e) {
            throw new RuntimeException("Error updating hotel availability for ID: " + hotel.getHotelId(), e);
        }

        return "Booking cancelled successfully.";
    }
}
