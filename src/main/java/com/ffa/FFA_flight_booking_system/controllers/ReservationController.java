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
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<Object> getReservationByReservationNumber(@PathVariable String reservationNumber) {
        return ResponseEntity.ok().body(reservationService.getReservationByReservationNumber(reservationNumber));
    }

    @GetMapping("/download/{reservationNumber}")
    public ResponseEntity<byte[]> generateCsv(@PathVariable String reservationNumber, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Validation Checks
            if (userDetails == null || reservationNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            ReservationDTO dto = reservationService.getReservationByReservationNumber(reservationNumber);
            Reservation reservation = reservationService.toReservation(dto);

            String currentUsername = userDetails.getUsername();
            UserDTO currentDTO = userService.getUser(currentUsername);
            User currentUser = userService.toUser(currentDTO);

            if (!currentUsername.equals(reservation.getUser().getUsername()) && !currentUser.hasAuthority("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reservation.csv");

            byte[] bytes = csvGeneratorUtil.generateCsv(reservationNumber).getBytes();

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // POST MAPPING, SENDING DATA

    @PostMapping("/create_reservation")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.toUser(userService.getUser(reservationDTO.getUsername()));
            Flight flight = flightService.toFlight(flightService.getFlightByFlightNumber(reservationDTO.getFlightNumber()));

            // Validation Checks
            if (userDetails == null || user == null || flight == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            String currentUsername = userDetails.getUsername();
            UserDTO dto = userService.getUser(currentUsername);
            User currentUser = userService.toUser(dto);

            // Can only create reservation for other profiles, except ADMIN
            if (!currentUsername.equals(reservationDTO.getUsername()) && !currentUser.hasAuthority("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Can only make reservation if flight capacity is not met
            if (flight.getAirplane().getAirplaneCapacity() >= flight.getReservations().size()) {
                throw new CapacityExceededException("This flight is fully booked");
            }


            return reservationService.createReservation(reservationDTO);
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create_reservations")
    public List<Reservation> createReservations(List<Reservation> reservations) {
        return reservationService.createReservations(reservations);
    }

}
