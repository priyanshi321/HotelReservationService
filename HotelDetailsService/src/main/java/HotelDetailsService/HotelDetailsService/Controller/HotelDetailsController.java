package HotelDetailsService.HotelDetailsService.Controller;
import HotelDetailsService.HotelDetailsService.Entity.HotelDetails;
import HotelDetailsService.HotelDetailsService.Service.HotelDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hotels")
@Validated
public class HotelDetailsController {

    @Autowired
    private HotelDetailsServiceImpl hotelDetailsService;

    @PostMapping
    public HotelDetails createHotelDetails(@RequestBody @Valid HotelDetails hotelDetails) {
        return hotelDetailsService.createHotelDetails(hotelDetails);
    }

    @DeleteMapping("/{hotelId}")
    public String deleteHotelDetails(@PathVariable Long hotelId) {
        return hotelDetailsService.deleteHotelDetails(hotelId);
    }

    @GetMapping
    public List<HotelDetails> getAllHotelDetails() {
        return hotelDetailsService.getAllHotelDetails();
    }

    @GetMapping("/{hotelId}")
    public HotelDetails getHotelDetailsById(@PathVariable Long hotelId) {
        return hotelDetailsService.getHotelDetailsById(hotelId);
    }
    @PutMapping("/{hotelId}")
    public HotelDetails updateHotelDetails(@PathVariable Long hotelId, @RequestBody HotelDetails updatedDetails) {
        return hotelDetailsService.updateHotelDetails(hotelId, updatedDetails);
    }
}
