package com.ffa.FFA_flight_booking_system.dto;

import com.ffa.FFA_flight_booking_system.models.Airplane;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class AirportDTO {
    @NotBlank
    public String airportName;
    @NotBlank
    public String airportCountry;
    @NotBlank
    public String airportCity;
    private Set<Airplane> airplanes;

    public AirportDTO(String airportName, String airportCountry, String airportCity) {
        this.airportName = airportName;
        this.airportCountry = airportCountry;
        this.airportCity = airportCity;
    }

    public AirportDTO() {
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public void setAirportCountry(String airportCountry) {
        this.airportCountry = airportCountry;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public void setAirportCity(String airportCity) {
        this.airportCity = airportCity;
    }

    public Set<Airplane> getAirplanes() {
        return airplanes;
    }

    public void setAirplanes(Set<Airplane> airplanes) {
        this.airplanes = airplanes;
    }
}
