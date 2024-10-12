package HotelDetailsService.HotelDetailsService.Service;
import HotelDetailsService.HotelDetailsService.Entity.HotelDetails;

import java.util.List;

public interface HotelDetailsService {

    HotelDetails createHotelDetails(HotelDetails hotelDetails);

    String deleteHotelDetails(long hotelId);

    List<HotelDetails> getAllHotelDetails();

    HotelDetails getHotelDetailsById(long hotelId);
    HotelDetails updateHotelDetails(Long hotelId, HotelDetails updatedDetails);
}
