package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.services.AirplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {
    private final AirplaneService airplaneService;

    @Autowired
    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping
    public ResponseEntity<List<Airplane>> getAllAirplanes() {
        return ResponseEntity.ok().body(airplaneService.getAllAirplanes());
    }

    @GetMapping("/{airplaneCode}")
    public ResponseEntity<Airplane> getAirplaneByAirplaneCode(@PathVariable String airplaneCode) {
        return ResponseEntity.ok().body(airplaneService.getAirplaneByAirplaneCode(airplaneCode));
    }

    // POST MAPPING, SAVING DATA

    @PostMapping("/create_airplane")
    public ResponseEntity<Airplane> saveAirplane(@RequestBody Airplane airplane) {
        return ResponseEntity.ok().body(airplaneService.saveAirplane(airplane));
    }

    @PostMapping("create_multiple_airplanes")
    public ResponseEntity<List<Airplane>> saveAirplanes(@RequestBody List<Airplane> airplanes) {
        return ResponseEntity.ok().body(airplaneService.saveAirplanes(airplanes));
    }

}
