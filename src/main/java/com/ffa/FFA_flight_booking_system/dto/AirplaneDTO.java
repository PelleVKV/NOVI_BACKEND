package com.ffa.FFA_flight_booking_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class AirplaneDTO {
    @NotBlank
    private String airplaneCode;
    @NotBlank
    private String airplaneType;
    @NotNull
    private int airplaneCapacity;
    @NotBlank
    private String residingAirport;
    @JsonIgnore
    private Set<Flight> flights;

    public AirplaneDTO(String airplaneCode, String airplaneType, int airplaneCapacity, String residingAirport, Set<Flight> flights) {
        this.airplaneCode = airplaneCode;
        this.airplaneType = airplaneType;
        this.airplaneCapacity = airplaneCapacity;
        this.residingAirport = residingAirport;
        this.flights = flights;
    }

    public AirplaneDTO() {
    }

    public String getAirplaneCode() {
        return airplaneCode;
    }

    public void setAirplaneCode(String airplaneCode) {
        this.airplaneCode = airplaneCode;
    }

    public String getAirplaneType() {
        return airplaneType;
    }

    public void setAirplaneType(String airplaneType) {
        this.airplaneType = airplaneType;
    }

    public int getAirplaneCapacity() {
        return airplaneCapacity;
    }

    public void setAirplaneCapacity(int airplaneCapacity) {
        this.airplaneCapacity = airplaneCapacity;
    }

    public String getResidingAirport() {
        return residingAirport;
    }

    public void setResidingAirport(String residingAirport) {
        this.residingAirport = residingAirport;
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }
}
