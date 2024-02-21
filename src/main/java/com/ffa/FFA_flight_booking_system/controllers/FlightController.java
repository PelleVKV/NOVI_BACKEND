package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping
    public ResponseEntity<ArrayList<Flight>> getAllFlights() {
        return new ResponseEntity<>(new ArrayList<>(flightService.getAllFlights()), HttpStatus.OK);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<Object> getFlightByFlightNumber(@PathVariable String flightNumber) {
        return ResponseEntity.ok().body(flightService.getFlightByFlightNumber(flightNumber));
    }

    // POST MAPPING, SENDING DATA

    @PostMapping("/create_flight")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        return new ResponseEntity<>(flightService.saveFlight(flight), HttpStatus.CREATED);
    }

    @PostMapping("/create_multiple_flights")
    public ResponseEntity<List<Flight>> createMultipleFlights(@RequestBody List<Flight> flights) {
        return new ResponseEntity<>(flightService.saveFlights(flights), HttpStatus.CREATED);
    }

}
