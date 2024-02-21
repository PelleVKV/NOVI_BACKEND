package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.services.AirplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/create")
    public ResponseEntity<Airplane> saveAirplane(@RequestBody Airplane airplane) {
        return ResponseEntity.ok().body(airplaneService.saveAirplane(airplane));
    }

    @PostMapping("/create_multiple")
    public ResponseEntity<List<Airplane>> saveAirplanes(@RequestBody List<Airplane> airplanes) {
        return ResponseEntity.ok().body(airplaneService.saveAirplanes(airplanes));
    }

    // DELETE MAPPING, DELETING DATA

    @DeleteMapping("/delete/{airplaneCode}")
    public ResponseEntity<String> deleteAirplane(@PathVariable String airplaneCode) {
        try {
            Airplane airplane = airplaneService.getAirplaneByAirplaneCode(airplaneCode);
            if (airplane != null) {
                airplaneService.deleteAirplane(airplaneCode);
                return new ResponseEntity<>("Airplane succesfully deleted.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Something went wrong deleting airplane", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
