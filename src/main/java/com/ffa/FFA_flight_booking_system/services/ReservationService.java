package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.dto.ReservationDTO;
import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.models.User;
import com.ffa.FFA_flight_booking_system.repositories.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final FlightService flightService;

    public ReservationService(ReservationRepository reservationRepository, UserService userService, FlightService flightService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.flightService = flightService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public ReservationDTO getReservationByReservationNumber(String reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);
        return fromReservation(reservation);
    }

    public List<Reservation> createReservations(List<Reservation> reservations) {
        return reservationRepository.saveAll(reservations);
    }

    public ResponseEntity<ReservationDTO> createReservation(ReservationDTO reservationDTO) {
        try {
            if (reservationDTO == null || userService.getUser(reservationDTO.getUsername()) == null ||
                    flightService.getFlightByFlightNumber(reservationDTO.getFlightNumber()) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Reservation reservation = toReservation(reservationDTO);
            reservationRepository.save(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    public static ReservationDTO fromReservation(Reservation reservation) {
        var dto = new ReservationDTO();
        dto.setReservationNumber(reservation.getReservationNumber());
        dto.setUsername(reservation.getUser().getUsername());
        dto.setFlightNumber(reservation.getFlight().getFlightNumber());
        return dto;
    }

    public Reservation toReservation(ReservationDTO dto) {
        var reservation = new Reservation();

        reservation.setReservationNumber(dto.getReservationNumber());

        UserDTO userDTO = userService.getUser(dto.getUsername());
        if (userDTO != null) {
            User user = userService.toUser(userDTO);
            reservation.setUser(user);
        }

        FlightDTO flightDTO = flightService.getFlightByFlightNumber(dto.getFlightNumber());
        if (flightDTO != null) {
            Flight flight = flightService.toFlight(flightDTO);
            reservation.setFlight(flight);
        }

        return reservation;
    }

}
