package com.ffa.FFA_flight_booking_system.dto;

import javax.validation.constraints.NotBlank;

public class ReservationDTO {
    public String reservationNumber;
    @NotBlank
    public String username;
    @NotBlank
    public String flightNumber;

    public ReservationDTO(String reservationNumber, String username, String flightNumber) {
        this.reservationNumber = reservationNumber;
        this.username = username;
        this.flightNumber = flightNumber;
    }

    public ReservationDTO() {

    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}

