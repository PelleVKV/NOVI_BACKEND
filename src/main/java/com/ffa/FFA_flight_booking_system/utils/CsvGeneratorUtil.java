package com.ffa.FFA_flight_booking_system.utils;

import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.models.User;
import com.ffa.FFA_flight_booking_system.services.ReservationService;
import com.ffa.FFA_flight_booking_system.services.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CsvGeneratorUtil {
    private static final String CSV_HEADER = "Reservation Number,Username,Flight Number\n";

    private final UserService userService;
    private final ReservationService reservationService;

    public CsvGeneratorUtil(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    public String generateCsv(String reservationNumber) {
        try {
            StringBuilder csv = new StringBuilder();
            csv.append(CSV_HEADER);

            List<Reservation> reservations = reservationService.getAllReservations();
            for (Reservation reservation : reservations) {
                if (reservation.getReservationNumber().equals(reservationNumber)) {
                    csv.append(reservation.getReservationNumber()).append(",")
                            .append(reservation.getUser().getUsername()).append(",")
                            .append(reservation.getFlight().getFlightNumber()).append(",")
                            .append(reservation.getFlight().getEtd()).append(",")
                            .append(reservation.getFlight().getEta()).append(",")
                            .append(reservation.getFlight().getDepartureAirport().getAirportName()).append(",")
                            .append(reservation.getFlight().getArrivalAirport().getAirportName()).append("\n");
                }
            }

            return csv.toString();
        } catch (Exception e) {
            return "Error generating CSV: " + e.getMessage();
        }
    }
}
