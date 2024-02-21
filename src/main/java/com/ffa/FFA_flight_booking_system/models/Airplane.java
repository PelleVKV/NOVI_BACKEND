package com.ffa.FFA_flight_booking_system.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "airplanes")
public class Airplane {
    @Id
    @Column(name = "airplane_code")
    private String airplaneCode;

    @Column(name = "airplane_type", nullable = false)
    private String airplaneType;

    @Column(name = "airplane_capacity", nullable = false)
    private int airplaneCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_name", nullable = false)
    @JsonBackReference
    private Airport residingAirport;

    @JsonBackReference
    @OneToMany(
            targetEntity =  Flight.class,
            mappedBy = "airplane",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Flight> flights = new HashSet<>();

    public Airport getResidingAirport() {
        return residingAirport;
    }

    public void setResidingAirport(Airport residingAirport) {
        this.residingAirport = residingAirport;
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

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }
}
