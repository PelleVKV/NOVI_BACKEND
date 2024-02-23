package com.ffa.FFA_flight_booking_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffa.FFA_flight_booking_system.models.Reservation;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

public class FlightDTO {
    @NotBlank
    public String flightNumber;
    @NotBlank
    public LocalDateTime etd;
    @NotNull
    public LocalDateTime eta;
    @NotNull
    public String airplaneCode;
    @NotNull
    public int filledSeats;
    @NotBlank
    public String depAirportName;
    @NotBlank
    public String arrAirportName;
    @JsonIgnore
    private Set<Reservation> reservations;

    public FlightDTO(String flightNumber, LocalDateTime etd, LocalDateTime eta, String airplaneCode, int filledSeats, String depAirportName, String arrAirportName, Set<Reservation> reservations) {
        this.flightNumber = flightNumber;
        this.etd = etd;
        this.eta = eta;
        this.airplaneCode = airplaneCode;
        this.filledSeats = filledSeats;
        this.depAirportName = depAirportName;
        this.arrAirportName = arrAirportName;
        this.reservations = reservations;
    }

    public FlightDTO() {

    }

    @AssertTrue(message = "ETA must be later than ETD")
    private boolean isEtaLaterThanEtd() {
        return eta == null || eta.isAfter(etd);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDateTime getEtd() {
        return etd;
    }

    public void setEtd(LocalDateTime etd) {
        this.etd = etd;
    }

    public LocalDateTime getEta() {
        return eta;
    }

    public void setEta(LocalDateTime eta) {
        this.eta = eta;
    }

    public String getAirplaneCode() {
        return airplaneCode;
    }

    public void setAirplaneCode(String airplaneCode) {
        this.airplaneCode = airplaneCode;
    }

    public String getDepAirportName() {
        return depAirportName;
    }

    public void setDepAirportName(String depAirportName) {
        this.depAirportName = depAirportName;
    }

    public String getArrAirportName() {
        return arrAirportName;
    }

    public void setArrAirportName(String arrAirportName) {
        this.arrAirportName = arrAirportName;
    }

    public int getFilledSeats() {
        return filledSeats;
    }

    public void setFilledSeats(int filledSeats) {
        this.filledSeats = filledSeats;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
