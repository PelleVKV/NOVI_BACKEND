package com.ffa.FFA_flight_booking_system.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ffa.FFA_flight_booking_system.generators.PrefixCodeGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_number")
    @GenericGenerator(
            name = "reservation_number",
            strategy = "com.ffa.FFA_flight_booking_system.generators.PrefixCodeGenerator",
            parameters = {
                    @Parameter(name = PrefixCodeGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = PrefixCodeGenerator.VALUE_PREFIX_PARAMETER, value = "RES"),
                    @Parameter(name = PrefixCodeGenerator.VALUE_SUFFIX_PARAMETER, value = "FFA"),
                    @Parameter(name = PrefixCodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d")
            })
    @Column(name = "reservation_number", nullable = false, unique = true)
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    @JsonBackReference(value = "user-reservation")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_number", nullable = false)
    @JsonBackReference(value = "flight-reservation")
    private Flight flight;

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getReservations().remove(this);
        }

        this.user = user;

        if (user != null) {
            user.getReservations().add(this);
        }
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
