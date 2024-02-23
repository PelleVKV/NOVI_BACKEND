package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.controllers.ReservationController;
import com.ffa.FFA_flight_booking_system.dto.FlightDTO;
import com.ffa.FFA_flight_booking_system.dto.ReservationDTO;
import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.models.User;
import com.ffa.FFA_flight_booking_system.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final FlightService flightService;

    public ReservationService(ReservationRepository reservationRepository, UserService userService, FlightService flightService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.flightService = flightService;
    }

    public List<ReservationDTO> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            List<ReservationDTO> reservationDTOS = new ArrayList<>();
            for (Reservation reservation : reservations) {
                reservationDTOS.add(fromReservation(reservation));
            }
            return reservationDTOS;
        } catch (Exception e) {
            return null;
        }
    }

    public ReservationDTO getReservationByReservationNumber(String reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);
        return fromReservation(reservation);
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        try {
            // Extra validation check
            if (reservationDTO == null || userService.getUser(reservationDTO.getUsername()) == null ||
                    flightService.getFlightByFlightNumber(reservationDTO.getFlightNumber()) == null) {
                throw new NotFoundException("User or Flight not found");
            }

            Reservation reservation = toReservation(reservationDTO);
            reservationRepository.save(reservation);
            return reservationDTO;
        } catch (Exception e) {
            logger.error("Failed to create reservation {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create reservation", e);
        }
    }

    public void deleteReservation(ReservationDTO reservationDTO) throws NotFoundException {
        Reservation reservation = toReservation(reservationDTO);
        if (reservation != null) {
            reservationRepository.delete(reservation);
        } else {
            throw new NotFoundException("Reservation not found");
        }
    }

    public void deleteAllReservations() {
        try {
            reservationRepository.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting all reservations");
        }
    }

    public static ReservationDTO fromReservation(Reservation reservation) {
        var dto = new ReservationDTO();
        dto.setReservationNumber(reservation.getReservationNumber());
        dto.setUsername(reservation.getUser().getUsername());
        dto.setFlightNumber(reservation.getFlight().getFlightNumber());
        return dto;
    }

    public Reservation toReservation(ReservationDTO dto) throws NotFoundException {
        var reservation = new Reservation();

        reservation.setReservationNumber(dto.getReservationNumber());
        System.out.println("toReservation: " + dto.getReservationNumber());

        UserDTO userDTO = userService.getUser(dto.getUsername());
        if (userDTO != null) {
            User user = userService.toUser(userDTO);
            reservation.setUser(user);
        }

        FlightDTO flightDTO = flightService.getFlightByFlightNumber(dto.getFlightNumber());
        Flight flight = flightService.toFlight(flightDTO);
        if (flight != null) {
            reservation.setFlight(flight);
        }

        return reservation;
    }

}
