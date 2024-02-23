package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Flight;
import com.ffa.FFA_flight_booking_system.services.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
public class AirportController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping("/airport/all")
    public ResponseEntity<List<AirportDTO>> getAllAirports() {
        List<AirportDTO> airports = airportService.findAll();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("airport/{airportName}")
    public ResponseEntity<Object> findByAirportName(@PathVariable String airportName) {
        return ResponseEntity.ok().body(airportService.findByAirportName(airportName));
    }

    @GetMapping("atc/{airportName}/airplanes")
    public ResponseEntity<Set<Airplane>> getAvailableAirplanes(@PathVariable String airportName) {
        Airport airport = airportService.findByAirportName(airportName);

        Set<Airplane> airplanes = airport.getAirplanes();
        System.out.println("Retrieved airplanes: " + airplanes);

        return ResponseEntity.ok().body(airplanes);
    }

    // POST MAPPING, SENDING DATA

    @PostMapping(value = "/atc/airport", consumes = "application/json")
    public ResponseEntity<String> createAirport(@RequestBody AirportDTO airportDTO) {
        try {
            Airport airport = airportService.toAirport(airportDTO);

            // Validation check
            if (airport == null) {
                return ResponseEntity.badRequest().body("Error: invalid airport data");
            }

            // Clear representation of action result
            airportService.save(airportDTO);
            return ResponseEntity.ok().body("Success: created airport.");
        } catch (Exception e) {
            logger.error("Error creating airport: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: error creating airport; " + e.getMessage());
        }
    }

    @PostMapping("/atc/multiple_airport")
    public ResponseEntity<List<Airport>> createMultipleAirports(@RequestBody List<Airport> airports) {
        return ResponseEntity.ok().body(airportService.saveAll(airports));
    }

    // DELETE MAPPING, DELETE DATA

    @DeleteMapping("/atc/airport/{airportName}")
    public ResponseEntity<String> deleteAirport(@PathVariable String airportName) {
        try {
            if (airportName == null) {
                return ResponseEntity.badRequest().body("Airport details invalid");
            }

            airportService.deleteAirport(airportName);
            return ResponseEntity.ok().body("Successfully deleted airport " + airportName);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting airport " + e.getMessage());
        }
    }

    @DeleteMapping("/atc/airport/all")
    public ResponseEntity<String> deleteAllAirports() {
        try {
            airportService.deleteAllAirports();
            return ResponseEntity.ok().body("Successfully deleted all airports");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting all airports " + e.getMessage());
        }
    }

}