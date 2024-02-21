package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.dto.ReservationDTO;
import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.exceptions.CapacityExceededException;
import com.ffa.FFA_flight_booking_system.models.Authority;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.models.User;
import com.ffa.FFA_flight_booking_system.services.FlightService;
import com.ffa.FFA_flight_booking_system.services.ReservationService;
import com.ffa.FFA_flight_booking_system.services.UserService;
import com.ffa.FFA_flight_booking_system.utils.CsvGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final UserService userService;
    private final FlightService flightService;

    @Autowired
    private CsvGeneratorUtil csvGeneratorUtil;

    @Autowired
    public ReservationController(ReservationService reservationService, UserService userService, FlightService flightService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.flightService = flightService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<Object> getReservationByReservationNumber(@PathVariable String reservationNumber) {
        ReservationDTO reservationDTO = reservationService.getReservationByReservationNumber(reservationNumber);
        if (reservationDTO != null) {
            return ResponseEntity.ok().body(reservationDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{reservationNumber}")
    public ResponseEntity<byte[]> generateCsv(@PathVariable String reservationNumber, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Validation Checks
            if (userDetails == null || reservationNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            ReservationDTO dto = reservationService.getReservationByReservationNumber(reservationNumber);
            Reservation reservation = reservationService.toReservation(dto);

            UserDTO currentUser = userService.getUser(userDetails.getUsername());
            boolean hasAdminAuth = userService.hasAdminAuthority(currentUser);

            if (!currentUser.getUsername().equals(reservation.getUser().getUsername()) && !hasAdminAuth) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reservation.csv");

            byte[] bytes = csvGeneratorUtil.generateCsv(reservationNumber).getBytes();

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error downloading reservation with number {}: {}", reservationNumber, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST MAPPING, SENDING DATA

    @PostMapping("/create_reservation")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.toUser(userService.getUser(reservationDTO.getUsername()));
            FlightDTO flightDTO = flightService.getFlightByFlightNumber(reservationDTO.getFlightNumber());
            Flight flight = flightService.toFlight(flightDTO);
            // Validation Checks
            if (userDetails == null || user == null || flight == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            UserDTO currentUser = userService.getUser(userDetails.getUsername());
            boolean hasAdminAuth = userService.hasAdminAuthority(currentUser);

            // Can only create reservation for other profiles, except ADMIN
            if (!userDetails.getUsername().equals(reservationDTO.getUsername()) && !hasAdminAuth) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Can only make reservation if flight capacity is not met
            if (flight.getFilledSeats() >= flight.getAirplane().getAirplaneCapacity()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            flightService.incrementFilledSeats(flight.getFlightNumber(), 1);
            return ResponseEntity.ok().body(reservationDTO);
        } catch (Exception e) {
            logger.error("Error creating reservation with number {}: {}", reservationDTO.getReservationNumber(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // DELETE MAPPING, DELETE DATA

    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<ReservationDTO> deleteReservation(@PathVariable String reservationNumber, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ReservationDTO reservationDTO = reservationService.getReservationByReservationNumber(reservationNumber);

            UserDTO currentUser = userService.getUser(userDetails.getUsername());
            boolean hasAdminAuth = userService.hasAdminAuthority(currentUser);

            // Only able to delete if ADMIN
            if (!hasAdminAuth) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (reservationDTO != null) {
                reservationService.deleteReservation(reservationDTO);
                return ResponseEntity.ok(reservationDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting reservation with number {}: {}", reservationNumber, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
