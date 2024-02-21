package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {
    private final AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping
    public ResponseEntity<List<Airport>> getAllAirports() {
        return ResponseEntity.ok().body(airportService.findAll());
    }

    @GetMapping("/{airportName}")
    public ResponseEntity<Object> findByAirportName(@PathVariable String airportName) {
        return ResponseEntity.ok().body(airportService.findByAirportName(airportName));
    }

    // POST MAPPING, SENDING DATA

    @PostMapping("/create_airport")
    public ResponseEntity<Airport> createAirport(@RequestBody Airport airport) {
        return ResponseEntity.ok().body(airportService.save(airport));
    }

    @PostMapping("/create_multiple_airports")
    public ResponseEntity<List<Airport>> createMultipleAirports(@RequestBody List<Airport> airports) {
        return ResponseEntity.ok().body(airportService.saveAll(airports));
    }

}
