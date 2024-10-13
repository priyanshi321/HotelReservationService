package HotelBookingService.HotelBookingService.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_booking")
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @NotEmpty(message = "Username cannot be empty")
    @Column(name = "username", nullable = false)
    private String username;

    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotEmpty(message = "Email ID cannot be empty")
    @Email(message = "Invalid email format")
    @Column(name = "email_id", nullable = false)
    private String emailId;

    @NotNull(message = "Check-in date cannot be null")
    @FutureOrPresent(message = "Check-in date must be in the present or future")
    @Column(name = "checkin_date", nullable = false)
    private LocalDateTime checkinDate;

    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    @Column(name = "checkout_date", nullable = false)
    private LocalDateTime checkoutDate;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @NotNull(message = "Hotel ID cannot be null")
    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @NotEmpty(message = "Room type cannot be empty")
    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Positive(message = "Number of rooms must be positive")
    @Min(value = 1, message = "Number of rooms must be at least 1")
    @Column(name = "number_of_rooms", nullable = false)
    private int numberOfRooms;


    @Column(name = "booking_status", nullable = false)
    private String bookingStatus;

    @JsonIgnore
    @AssertTrue(message = "Check-out date must be after check-in date")
    public boolean isCheckOutDateAfterCheckInDate() {
        return checkinDate == null || checkoutDate == null || checkoutDate.isAfter(checkinDate);
    }

    public HotelBooking() {
    }

    public HotelBooking(Long bookingId, String username, String phoneNumber, String emailId, LocalDateTime checkinDate, LocalDateTime checkoutDate, String paymentStatus, Long hotelId, String roomType, int numberOfRooms, String bookingStatus) {
        this.bookingId = bookingId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.paymentStatus = paymentStatus;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.bookingStatus = bookingStatus;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public LocalDateTime getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDateTime checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDateTime getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDateTime checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "HotelBooking{" +
                "bookingId=" + bookingId +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", roomType='" + roomType + '\'' +
                ", numberOfRooms=" + numberOfRooms +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
