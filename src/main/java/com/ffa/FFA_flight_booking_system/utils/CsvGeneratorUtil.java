package com.ffa.FFA_flight_booking_system.utils;

import com.ffa.FFA_flight_booking_system.dto.ReservationDTO;
import com.ffa.FFA_flight_booking_system.services.FlightService;
import com.ffa.FFA_flight_booking_system.services.ReservationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvGeneratorUtil {
    private static final String CSV_HEADER = "Reservation Number,Username,Flight Number,ETD,ETA,Departure Airport,Arrival Airport\n";
    private final ReservationService reservationService;
    private final FlightService flightService;

    private CsvGeneratorUtil(ReservationService reservationService, FlightService flightService) {
        this.reservationService = reservationService;
        this.flightService = flightService;
    }

    public String generateCsv(String reservationNumber) {
        try {
            StringBuilder csv = new StringBuilder();
            csv.append(CSV_HEADER);

            List<ReservationDTO> reservations = reservationService.getAllReservations();
            for (ReservationDTO reservationDTO : reservations) {
                if (reservationDTO.getReservationNumber().equals(reservationNumber)) {
                    csv.append(reservationDTO.getReservationNumber()).append(",")
                            .append(reservationDTO.getUsername()).append(",")
                            .append(reservationDTO.getFlightNumber()).append(",")
                            .append(flightService.getFlightByFlightNumber(reservationDTO.flightNumber).getEtd()).append(",")
                            .append(flightService.getFlightByFlightNumber(reservationDTO.flightNumber).getEta()).append(",")
                            .append(flightService.getFlightByFlightNumber(reservationDTO.flightNumber).getDepAirportName()).append(",")
                            .append(flightService.getFlightByFlightNumber(reservationDTO.flightNumber).getArrAirportName()).append(",");
                }
            }

            return csv.toString();
        } catch (Exception e) {
            return "Error generating CSV: " + e.getMessage();
        }
    }
}
