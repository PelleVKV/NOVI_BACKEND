package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.models.Airplane;
import com.ffa.FFA_flight_booking_system.repositories.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;

    @Autowired
    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public List<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    public Airplane getAirplaneByAirplaneCode(String airplaneCode) {
        return airplaneRepository.findByAirplaneCode(airplaneCode).orElse(null);
    }

    public Airplane saveAirplane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public List<Airplane> saveAirplanes(List<Airplane> airplanes) {
        return airplaneRepository.saveAll(airplanes);
    }

}
