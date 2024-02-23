package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.AirplaneDTO;
import com.ffa.FFA_flight_booking_system.dto.AirportDTO;
import com.ffa.FFA_flight_booking_system.exceptions.NotFoundException;
import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.models.Airport;
import com.ffa.FFA_flight_booking_system.models.Reservation;
import com.ffa.FFA_flight_booking_system.services.AirplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class AirplaneController {
    private final AirplaneService airplaneService;

    @Autowired
    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping("/airplane/all")
    public ResponseEntity<List<AirplaneDTO>> getAllAirplanes() {
        List<AirplaneDTO> airplane = airplaneService.getAllAirplanes();
        return ResponseEntity.ok(airplane);
    }

    @GetMapping("/airplane/{airplaneCode}")
    public ResponseEntity<AirplaneDTO> getAirplaneByAirplaneCode(@PathVariable String airplaneCode) throws NotFoundException {
        try {
            AirplaneDTO airplaneDTO = airplaneService.getAirplaneByAirplaneCode(airplaneCode);
            return ResponseEntity.ok().body(airplaneDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST MAPPING, SAVING DATA

    @PostMapping("/atc/airplane")
    public ResponseEntity<String> saveAirplane(@RequestBody AirplaneDTO airplaneDTO) {
        try {
            if (airplaneDTO == null) {
                return ResponseEntity.badRequest().body("Error: invalid airplane data");
            }
            airplaneService.saveAirplane(airplaneDTO);
            return ResponseEntity.ok().body("Successfully created airplane");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving airplane" + e.getMessage());
        }
    }

    // PUT MAPPING, CHANGING DATA

    @PutMapping("/atc/airplane/{airplaneCode}")
    public ResponseEntity<String> updateAirplane(@PathVariable String airplaneCode, @RequestBody AirplaneDTO updatedAirplaneDTO) {
        try {
            airplaneService.updateAirplane(airplaneCode, updatedAirplaneDTO);
            return ResponseEntity.ok().body("Airplane successfully updated");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // DELETE MAPPING, DELETING DATA

    @DeleteMapping("/atc/airplane/{airplaneCode}")
    public ResponseEntity<String> deleteAirplane(@PathVariable String airplaneCode) {
        try {
            AirplaneDTO airplaneDTO = airplaneService.getAirplaneByAirplaneCode(airplaneCode);
            Airplane airplane = airplaneService.toAirplane(airplaneDTO);
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

    @DeleteMapping("/atc/airplane/all")
    public ResponseEntity<String> deleteAllAirplanes() {
        try {
            airplaneService.deleteAllAirplanes();
            return new ResponseEntity<>("Airplanes successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
