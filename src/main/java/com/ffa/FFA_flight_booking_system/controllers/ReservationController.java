package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.dto.ReservationDTO;
import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.exceptions.CapacityExceededException;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
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

import java.util.ArrayList;
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
    public List<ReservationDTO> getAllReservations() throws NotFoundException {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<ReservationDTO> getReservationByReservationNumber(@PathVariable String reservationNumber) {
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

            // Cannot download reservation from different user account, unless user got ADMIN auth
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
    public ResponseEntity<String> createReservation(@RequestBody ReservationDTO reservationDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.toUser(userService.getUser(reservationDTO.getUsername()));
            FlightDTO flightDTO = flightService.getFlightByFlightNumber(reservationDTO.getFlightNumber());
            Flight flight = flightService.toFlight(flightDTO);
            // Validation Checks
            if (userDetails == null || user == null || flight == null) {
                return ResponseEntity.badRequest().body("Error: invalid reservation details");
            }

            UserDTO currentUser = userService.getUser(userDetails.getUsername());
            boolean hasAdminAuth = userService.hasAdminAuthority(currentUser);

            // Can only create reservation for other profiles, except ADMIN
            if (!userDetails.getUsername().equals(reservationDTO.getUsername()) && !hasAdminAuth) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: Can only create reservation for other profiles");
            }

            // Validating scenarios of not unique id and where user already got reservation on flight
            List<Reservation> reservations = new ArrayList<>(flightDTO.getReservations());
            for (Reservation reservation : reservations) {
                if (reservation.getReservationNumber().equals(reservationDTO.reservationNumber)) {
                    return ResponseEntity.badRequest().body("Error: duplicate reservation number found");
                }
                if (reservation.getUser().getUsername().equals(reservationDTO.getUsername())) {
                    return ResponseEntity.badRequest().body("Error: duplicate reservation on user account found");
                }
            }

            // Can only make reservation if flight capacity is not met
            if (flight.getFilledSeats() >= flight.getAirplane().getAirplaneCapacity()) {
                return ResponseEntity.badRequest().body("Error: capacity of this flight is full");
            }

            // Success response and adding a filled seat to the reserved flight
            flightService.updateFilledSeats(flight.getFlightNumber(), 1);
            ReservationDTO savedReservation = reservationService.createReservation(reservationDTO);
            return ResponseEntity.ok()
                    .body("Success: created new reservation");
        } catch (Exception e) {
            logger.error("Error creating reservation {}: {}", reservationDTO.getReservationNumber(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error creating reservation " + e.getMessage());
        }
    }

    // PUT MAPPING, CHANGING DATA

    @PutMapping("/{reservationNumber}")
    public ResponseEntity<Object> updateReservationByReservationNumber(
            @PathVariable String reservationNumber,
            @RequestBody ReservationDTO updatedReservationDTO) {
        return reservationService.updateReservationByReservationNumber(reservationNumber, updatedReservationDTO);
    }

    // DELETE MAPPING, DELETE DATA

    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<String> deleteReservation(@PathVariable String reservationNumber, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ReservationDTO reservationDTO = reservationService.getReservationByReservationNumber(reservationNumber);

            UserDTO currentUser = userService.getUser(userDetails.getUsername());
            boolean hasAdminAuth = userService.hasAdminAuthority(currentUser);

            // Validation check
            if (reservationNumber == null) {
                return ResponseEntity.badRequest().body("Error: invalid reservation data");
            }

            // Only able to delete own reservation, unless got ADMIN auth
            if (!reservationDTO.getUsername().equals(currentUser.getUsername()) && !hasAdminAuth) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: cannot delete reservation that's not owned by current logged-in user");
            }

            // Success response and retracting a filled seat from flight on canceled reservation
            flightService.updateFilledSeats(reservationDTO.getFlightNumber(), -1);
            reservationService.deleteReservation(reservationDTO);
            return ResponseEntity.ok().body("Success: deleted reservation (" + reservationNumber + ")");
        } catch (Exception e) {
            logger.error("Error deleting reservation with number {}: {}", reservationNumber, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: error deleting reservation; " + e.getMessage());
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllReservations() {
        try {
            // Retracting a filled seat from the flight from every corresponding reservation
            List<ReservationDTO> reservationsDTOS = reservationService.getAllReservations();
            for (ReservationDTO reservationDTO: reservationsDTOS) {
                flightService.updateFilledSeats(reservationDTO.getFlightNumber(), -1);
            }

            reservationService.deleteAllReservations();
            return ResponseEntity.ok("Success: All reservations deleted");
        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.internalServerError().body("Error deleting reservations: " + e.getMessage());
        }
    }
}
