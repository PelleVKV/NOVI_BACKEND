package com.ffa.FFA_flight_booking_system.dto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class FlightDTO {
    @NotBlank
    public String flightNumber;
    @NotBlank
    public LocalDateTime etd;
    @NotBlank
    public LocalDateTime eta;
    @NotBlank
    public String airplaneCode;
    @NotBlank
    public String dep_airport_name;
    @NotBlank
    public String arr_airport_name;

    public FlightDTO(String flightNumber, LocalDateTime etd, LocalDateTime eta, String airplaneCode, String dep_airport_name, String arr_airport_name) {
        this.flightNumber = flightNumber;
        this.etd = etd;
        this.eta = eta;
        this.airplaneCode = airplaneCode;
        this.dep_airport_name = dep_airport_name;
        this.arr_airport_name = arr_airport_name;
    }

    public FlightDTO() {

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

    public String getDep_airport_name() {
        return dep_airport_name;
    }

    public void setDep_airport_name(String dep_airport_name) {
        this.dep_airport_name = dep_airport_name;
    }

    public String getArr_airport_name() {
        return arr_airport_name;
    }

    public void setArr_airport_name(String arr_airport_name) {
        this.arr_airport_name = arr_airport_name;
    }
}
