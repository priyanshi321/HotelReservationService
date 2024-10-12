package HotelDetailsService.HotelDetailsService.Repository;
import HotelDetailsService.HotelDetailsService.Entity.HotelDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelDetailsRepository extends JpaRepository<HotelDetails, Long> {

}
