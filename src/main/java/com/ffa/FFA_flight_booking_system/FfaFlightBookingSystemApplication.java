package com.ffa.FFA_flight_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Fake Flight Airlines Reservation System
 *
 * This application serves as a reservation system for Fake Flight Airlines, a virtual airline.
 * It manages user authentication, flight bookings, and airplane details.
 * The system is designed to handle user registrations, authentication using JWT, and the storage
 * of flight and airplane information in a PostgreSQL database. It includes features for creating,
 * updating, and deleting users, flights, and airplanes.
 *
 * Project: FFA Booking System
 * Author: Pelle van Ketwich Verschuur
 * Date: [Current Date] TODO: update date
 * Version: 1.0
 */

@SpringBootApplication
public class FfaFlightBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FfaFlightBookingSystemApplication.class, args);
	}

}
