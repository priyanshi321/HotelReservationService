package HotelDetailsService.HotelDetailsService.Service;
import HotelDetailsService.HotelDetailsService.Entity.HotelDetails;
import HotelDetailsService.HotelDetailsService.Repository.HotelDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelDetailsServiceImpl implements HotelDetailsService {

    @Autowired
    private HotelDetailsRepository hotelDetailsRepository;

    @Override
    public HotelDetails createHotelDetails(HotelDetails hotelDetails) {
        return hotelDetailsRepository.save(hotelDetails);
    }

    @Override
    public String deleteHotelDetails(long hotelId) {
        Optional<HotelDetails> details = hotelDetailsRepository.findById(hotelId);
        if (details.isPresent()) {
            hotelDetailsRepository.delete(details.get());
            return "Hotel details deleted successfully.";
        }
        return "Hotel details not found.";
    }

    @Override
    public List<HotelDetails> getAllHotelDetails() {
        return hotelDetailsRepository.findAll();
    }

    @Override
    public HotelDetails getHotelDetailsById(long hotelId) {
        return hotelDetailsRepository.findById(hotelId).orElse(null);
    }
    public HotelDetails updateHotelDetails(Long hotelId, HotelDetails updatedDetails) {
        HotelDetails existingHotel = hotelDetailsRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        existingHotel.setRoomAvailability(updatedDetails.getRoomAvailability());
        existingHotel.setCharge(updatedDetails.getCharge());
        existingHotel.setStatus(updatedDetails.getStatus());

        return hotelDetailsRepository.save(existingHotel);
    }
}
