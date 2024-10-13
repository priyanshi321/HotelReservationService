package HotelDetailsService.HotelDetailsService.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "hotel_details")
public class HotelDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    @Positive(message = "Hotel ID must be positive")
    private Long hotelId;

    @ElementCollection
    @CollectionTable(name = "room_availability", joinColumns = @JoinColumn(name = "hotel_id"))
    @MapKeyColumn(name = "room_type")
    @Column(name = "available_rooms")
    private Map<String, Integer> roomAvailability = new HashMap<>();

    @Column(name = "charge", nullable = false)
    private Double charge;

    @Column(name = "status", nullable = false)
    private String status;

    public HotelDetails() {
        roomAvailability.put("SingleRoom", 10);
        roomAvailability.put("DoubleRoom", 10);
        roomAvailability.put("DeluxeRoom", 10);
    }



    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(Map<String, Integer> roomAvailability) {
        this.roomAvailability = roomAvailability;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HotelDetails{" +
                "hotelId=" + hotelId +
                ", roomAvailability=" + roomAvailability +
                ", charge=" + charge +
                ", status='" + status + '\'' +
                '}';
    }
}
