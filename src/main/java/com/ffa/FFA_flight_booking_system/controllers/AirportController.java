package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.services.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/airports")
public class AirportController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

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

    @GetMapping("/{airportName}/airplanes")
    public ResponseEntity<Set<Airplane>> getAvailableAirplanes(@PathVariable String airportName) {
        AirportDTO airportDTO = airportService.findByAirportName(airportName);
        Airport airport = airportService.toAirport(airportDTO);

        Set<Airplane> airplanes = airport.getAirplanes();
        System.out.println("Retrieved airplanes: " + airplanes);

        return ResponseEntity.ok().body(airplanes);
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
