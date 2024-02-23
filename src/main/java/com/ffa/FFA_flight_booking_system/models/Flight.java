package com.ffa.FFA_flight_booking_system.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ffa.FFA_flight_booking_system.generators.PrefixCodeGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_number")
    @GenericGenerator(
            name = "flight_number",
            strategy = "com.ffa.FFA_flight_booking_system.generators.PrefixCodeGenerator",
            parameters = {
                    @Parameter(name = PrefixCodeGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = PrefixCodeGenerator.VALUE_PREFIX_PARAMETER, value = "FFA"),
                    @Parameter(name = PrefixCodeGenerator.VALUE_SUFFIX_PARAMETER, value = "FL"),
                    @Parameter(name = PrefixCodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            })
    @Column(name = "flight_number", nullable = false, unique = true)
    private String flightNumber;

    @Column(name = "estimated_time_of_departure", nullable = false)
    private LocalDateTime etd;

    @Column(name = "estimated_time_of_arrival", nullable = false)
    private LocalDateTime eta;

    @Column(name = "filled_seats")
    private int filledSeats;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airplane_code")
    @JsonManagedReference
    private Airplane airplane;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_airport_name")
    @JsonBackReference
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_airport_name")
    @JsonBackReference
    private Airport arrivalAirport;

    @OneToMany(
            targetEntity =  Reservation.class,
            mappedBy = "flight",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private Set<Reservation> reservations = new HashSet<>();

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

    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public int getFilledSeats() {
        return filledSeats;
    }

    public void setFilledSeats(int filledSeats) {
        this.filledSeats = filledSeats;
    }
}
