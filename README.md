Hotel Reservation Service
This project is a Hotel Reservation Service that comprises two main services: HotelBookingService and HotelDetailsService.

HotelBookingService
The HotelBookingService manages hotel bookings and includes the following key booking entities: Booking ID, Username, Phone Number, Email ID, Check-in Date, Check-out Date, Payment Status, Booking Status, and additional details necessary for booking.

HotelDetailsService
The HotelDetailsService provides information about hotels, including types of rooms available, cost of each room, and number of rooms for each type. 
Both serivces communicating using RestTemplate.

The API offers the following functionalities:

Create Booking: This functionality allows users to create a booking for a specific hotel ID. It checks whether rooms are available for the requested room type, ensuring that all necessary validations are performed.
- Room Availability: The service verifies room availability, ensuring that the requested room type has enough rooms available for booking.
- Date Validation: The service checks that the check-in date is before the check-out date.
- Validation of Number of Rooms: The number of rooms requested cannot be negative, and various other validations are performed to ensure the integrity of the booking process.
- Get Booking Details by ID: Retrieve details of a booking using its unique ID.
- Update Booking Details: Update existing booking information as needed.
- cancel Booking: This functionality allows users to delete an existing booking. It ensures that the booking is canceled and the associated room availability is updated accordingly.
- This project aims to streamline the hotel booking process while ensuring that all necessary validations and checks are in place to provide a seamless experience for users.
